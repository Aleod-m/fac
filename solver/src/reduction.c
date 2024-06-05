#include "reduction.h"
#include "Z3Tools.h"
#include "game.h"
#include <stdlib.h>

/* The code of this formula is given to you so that all of you will have the same notations for the variables.
    You should call this function every time you need to use the variable [row,col] either in constructing the formula or determining their value in a model. It is not necessary to create them only once (and it is not possible). Every call with the same argument will return the same function.
*/
Z3_ast getVariableCell(Z3_context ctx, uint row, uint col)
{
    char name[40];
    snprintf(name, 40, "[%d,%d]", row, col);
    return mk_bool_var(ctx, name);
}

/**
 * @brief Generates a formula that is the and of the formulae from @p left and @p right.
 *
 * @param ctx The solver context.
 * @param formulae The left part of the and formulae.
 * @param formulae The right part of the and formulae.
 * @return Z3_ast The obtained formula.
 */
Z3_ast mk_and(Z3_context ctx, Z3_ast r, Z3_ast l)
{
    Z3_ast args[2];
    args[0] = r;
    args[1] = l;
    return Z3_mk_and(ctx, 2, args);
}

/**
 * @brief Generates a formula that is the nand of the formulae from @p left and @p right.
 *
 * @param ctx The solver context.
 * @param formulae The left part of the and formulae.
 * @param formulae The right part of the and formulae.
 * @return Z3_ast The obtained formula.
 */
Z3_ast mk_nand(Z3_context ctx, Z3_ast r, Z3_ast l)
{
    Z3_ast args[2];
    args[0] = r;
    args[1] = l;
    return Z3_mk_not(ctx, Z3_mk_and(ctx, 2, args));
}

/**
 * @brief Generates a formula that is the or of the formulae from @p left and @p right.
 *
 * @param ctx The solver context.
 * @param formulae The left part of the and formulae.
 * @param formulae The right part of the and formulae.
 * @return Z3_ast The obtained formula.
 */
Z3_ast mk_or(Z3_context ctx, Z3_ast r, Z3_ast l)
{
    Z3_ast args[2];
    args[0] = r;
    args[1] = l;
    return Z3_mk_or(ctx, 2, args);
}

Z3_ast atMostFormula(Z3_context ctx, Z3_ast *formulae, uint size)
{
    /*You might need this formula, implement it as needed.*/
    return Z3_mk_true(ctx);
}

// Helper function that makes a formula where exactly @k from the @num input formulas stored in @vars must be true. 
Z3_ast exactNumFormula(Z3_context ctx, Z3_ast *vars, uint num, int k)
{
    /*Formula corresponding to the one provided in the subject.*/
    if (k == 0) {
        return Z3_mk_not(ctx, Z3_mk_or(ctx, num, vars));
    } else if (k == num) {
        return Z3_mk_and(ctx, num, vars);
    } else if (k > num) {
        return Z3_mk_false(ctx);
    } else {
        Z3_ast and1 = mk_and(ctx, vars[0], exactNumFormula(ctx, vars+1, num-1, k-1));
        Z3_ast and2 = mk_and(ctx, Z3_mk_not(ctx, vars[0]),exactNumFormula(ctx, vars+1, num-1, k));
        return mk_or(ctx, and1, and2);
    }
}

/**
 * @brief Generates a formula stating that at least one of the formulae from @p vars is true.
 *
 * @param ctx The solver context.
 * @param vars The input formulae.
 * @param num The number of formulae.
 * @param k The minimum number of formulae that must be true.
 * @return Z3_ast The obtained formula.
 */
Z3_ast at_least(Z3_context ctx, Z3_ast *vars, uint num, int k)
{
    if (k == 0) {
        return Z3_mk_true(ctx);
    } else if (k == num) {
        return Z3_mk_and(ctx, num, vars);
    } else if (k > num) {
        return Z3_mk_false(ctx);
    } else {
        Z3_ast and1 = mk_and(ctx, vars[0], at_least(ctx, vars+1, num-1, k-1));
        Z3_ast and2 = mk_and(ctx, Z3_mk_not(ctx, vars[0]), at_least(ctx, vars+1, num-1, k));
        return mk_or(ctx, and1, and2);
    }
}

/**
 * @brief Generates a formula representing the rule: no lights must be aligned.
 *
 * @param ctx The solver context.
 * @param g The game.
 * @return Z3_ast The obtained formula.
 */
Z3_ast phi_rule_1(Z3_context ctx, game g) {
    uint cpt = 0;
    uint nb_row = game_nb_rows(g);
    uint nb_col = game_nb_cols(g);
    Z3_ast *args = (Z3_ast *)malloc(sizeof(Z3_ast) * (nb_row * nb_col) * (nb_row + nb_col)); // to check
    for(uint i = 0; i < nb_row; i++) {
        for(uint j = 0; j < nb_col; j++) {
            if(!game_is_blank(g, i, j)) continue;
            uint ip = i+1;
            while(ip < nb_row && game_is_blank(g, ip, j)) {
                args[cpt++] = mk_nand(ctx, getVariableCell(ctx, i, j), getVariableCell(ctx, ip, j));
                ip++;
            }
            uint jp = j+1;
            while(jp < nb_col && game_is_blank(g, i, jp)) {
                args[cpt++] = mk_nand(ctx, getVariableCell(ctx, i, j), getVariableCell(ctx, i, jp));
                jp++;
            }
        }
    }
    Z3_ast result = Z3_mk_and(ctx, cpt, args); 
    free(args);
    return result;
}

/*
 * @brief Fill the array @p squares with all the visible_squares form the input square at @p i, @p j.
 *
 * @param ctx The solver context.
 * @param g The game.
 * @param i The row of the input square.
 * @param j The column of the input square.
 * @param squares The visible squares from the input square.
 * @return uint The number of visible squares found.
 */
uint visible(Z3_context ctx, game g, int i, int j, Z3_ast *squares) {
    uint nb_row = game_nb_rows(g);
    uint nb_col = game_nb_cols(g);
    uint nb = 0;
    for(int ip = i + 1;;) {
        if(ip < nb_row && game_is_blank(g, ip, j)) {
            squares[nb++] = getVariableCell(ctx, ip, j);
            ip++;
        } else {
            break;
        }
    }
    for(int ip = i - 1;;) {
        if (ip >= 0 && game_is_blank(g, ip, j)) {
            squares[nb++] = getVariableCell(ctx, ip, j);
            ip--;
        } else {
            break;
        }
    }
    for(int jp = j+1;;) {
        if(jp < nb_col && game_is_blank(g, i, jp)) {
            squares[nb++] = getVariableCell(ctx, i, jp);
            jp++;
        } else {
            break;
        }
    }
    for(int jp = j - 1;;) {
        if(jp >= 0 && game_is_blank(g, i, jp)) {
            squares[nb++] = getVariableCell(ctx, i, jp);
            jp--;
        } else {
            break;
        }
    }
    return nb;
}

/*
 * @brief Generates a formula representing the rule: all square must be lit.
 *
 * @param ctx The solver context.
 * @param g The game.
 * @return Z3_ast the obtained formula.
 */
Z3_ast phi_rule_2(Z3_context ctx, game g) {
    uint nb_row = game_nb_rows(g);
    uint nb_col = game_nb_cols(g);
    uint cpt_and = 0;
    Z3_ast *args_and = (Z3_ast *)malloc(sizeof(Z3_ast) * (nb_row * nb_col)); // to check
    for(int i = 0; i < nb_row; i++) {
        for(int j = 0; j < nb_col; j++) {
            if (!game_is_blank(g, i, j)) continue;
            Z3_ast *visible_squares= (Z3_ast *)malloc(sizeof(Z3_ast)*(nb_row + nb_col));
            uint nb_visible = visible(ctx, g, i, j, visible_squares);
            visible_squares[nb_visible++] = getVariableCell(ctx, i, j);
            args_and[cpt_and++] = at_least(ctx, visible_squares, nb_visible, 1);
            free(visible_squares);
        }
    }
    Z3_ast result = Z3_mk_and(ctx, cpt_and, args_and);
    free(args_and);
    return result;
}


/*
 * @brief Fill the array @p vars with the empty squares neighboring the input square at @p i, @p j.
 *
 * @param ctx The solver context.
 * @param g The game.
 * @param i The row of the input square.
 * @param j The column of the input square.
 * @param vars The neighboring squares.
 * @return uint the number of neighbors.
 */
uint neighbors_vars(Z3_context ctx, game g, int i, int j, Z3_ast *vars) {
    uint nb = 0;
    if (j - 1 >= 0 && game_is_blank(g, i, j - 1)) {
        vars[nb++] = getVariableCell(ctx, i, j-1);
    }
    if (j + 1 < game_nb_cols(g) && game_is_blank(g, i, j + 1)) {
        vars[nb++] = getVariableCell(ctx, i, j+1);
    }
    if (i - 1 >= 0 && game_is_blank(g, i - 1, j)) {
        vars[nb++] = getVariableCell(ctx, i - 1, j);
    }
    if (i + 1 < game_nb_rows(g) && game_is_blank(g, i + 1, j)) {
        vars[nb++] = getVariableCell(ctx, i + 1, j);
    }
    return nb;
}

/*
 * @brief Compute a formula which represent the rule: wall must have the number written on them of adjacent lights.
 *
 * @param ctx The solver context.
 * @param g The game.
 * @return Z3_ast The computed formula.
 */
Z3_ast phi_rule_3(Z3_context ctx, game g) {
    uint nb_row = game_nb_rows(g);
    uint nb_col = game_nb_cols(g);
    uint cpt_and = 0;
    Z3_ast *args_and = (Z3_ast *)malloc(sizeof(Z3_ast) * nb_col * nb_row);
    for(uint i = 0; i < nb_row; i++) {
        for(uint j = 0; j < nb_col; j++) {
            if (game_is_black(g, i, j) && game_get_black_number(g, i, j) != -1) {
                Z3_ast neighbors[4];
                uint n = neighbors_vars(ctx, g, i, j, neighbors);
                uint k = game_get_black_number(g, i , j);
                args_and[cpt_and++] = exactNumFormula(ctx, neighbors, n, k);
            }
        }
    }
    Z3_ast result = Z3_mk_and(ctx, cpt_and, args_and);
    free(args_and);
    return result;
}


/*
 * @brief Compute the formula wich is the combinaison of the 3 rules. 
 *
 * @param ctx The solver context.
 * @param g The game.
 * @return Z3_ast The computed formula.
 */
Z3_ast gameFormula(Z3_context ctx, game g)
{
    /*Implement the reduction here. You should cut it in subfunction, for example
     * one per rule of the game.
     */
    Z3_ast rules[3];
    rules[0] = phi_rule_1(ctx, g);
    rules[1] = phi_rule_2(ctx, g);
    rules[2] = phi_rule_3(ctx, g);
    return Z3_mk_and(ctx, 3, rules);
}

Z3_ast other_sol_formula(Z3_context ctx, game g, Z3_ast formula, Z3_model model)
{
    /*
     * Implement here the formula described in the bonus of the reduction part.
     * Note that you do not have to regenerate the formula describing the game 
     * as it is given in argument, you simply have to complete it.
     */
    return (Z3_mk_true(ctx));
}

void applySolutionToGame(Z3_context ctx, Z3_model model, game g)
{
    /*
     * Investigates the model obtained to place lamps in g.
     * You can obtain the value of a variable with valueOfVarInModel.
    */

    for (int i = 0; i < game_nb_rows(g); i++) {
        for (int j = 0; j < game_nb_cols(g); j++) {
            if (game_is_blank(g, i, j)) {
                if (valueOfVarInModel(ctx, model, getVariableCell(ctx, i, j))) {
                    game_set_square(g, i, j, LIGHTBULB);
                }
            }
        }
    }
}
