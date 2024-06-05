#include <stdio.h>
#include <stdlib.h>
#include "brute_force.h"
#include "game.h"
#include "assert.h"
#include <math.h>


bool left_lighted(game g, uint row, uint col) {          
    assert(g);
    assert(row < game_nb_rows(g));
    assert(col < game_nb_cols(g));
    for (int left = col - 1; left >= 0; left--) {
        if (game_is_black(g, row, left)) {  
            break;
        }
        if (game_get_square(g, row, left) == 1) {  
            return true;
        }
    }
    return false;
}

bool right_lighted(game g, uint row, uint col) {          
    assert(g);
    assert(row < game_nb_rows(g));
    assert(col < game_nb_cols(g));
    for (int right = col + 1; right < game_nb_cols(g); right++) {
        if (game_is_black(g, row, right)) {             
            break;
        }
        if (game_get_square(g, row, right) == 1) { 
            return true;
        }
    }
    return false;
}

bool up_lighted(game g, uint row, uint col) {         
    assert(g);
    assert(row < game_nb_rows(g));
    assert(col < game_nb_cols(g));
    for (int up = row - 1; up >= 0; up--) {
        if (game_is_black(g, up, col)) {             
            break;
        }
        if (game_get_square(g, up, col) == 1) {     
            return true;
        }
    }
    return false;
}

bool down_lighted(game g, uint row, uint col) {          
    assert(g);
    assert(row < game_nb_rows(g));
    assert(col < game_nb_cols(g));
    for (int down = row + 1; down < game_nb_rows(g); down++) {
        if (game_is_black(g, down, col)) {             
            break;
        }
        if (game_get_square(g, down, col) == 1) {   
            return true;
        }
    }
    return false;
}

//-------------------------------------------------------------------------------------------------------------------------//
//                                                 RULE 1                                                                  //
//-------------------------------------------------------------------------------------------------------------------------//


bool all_cells_lighted(game g) {            
    assert(g);
    for (int i = 0; i < game_nb_rows(g); i++) {
        for (int j = 0; j < game_nb_cols(g); j++) {

            if (game_get_square(g, i, j) == 1) {
                continue;
            }
            bool eclaire = (left_lighted(g, i, j) || right_lighted(g, i, j) || up_lighted(g, i, j) || down_lighted(g, i, j));
            if (!eclaire) {
                return false;
            }
        }   
    }
    return true;
}

//-------------------------------------------------------------------------------------------------------------------------//
//                                                 RULE 2                                                                  //
//-------------------------------------------------------------------------------------------------------------------------//

bool no_lights_aligned(game g) {             
    assert(g);
    for (int i = 0; i < game_nb_rows(g); i++) {
        for (int j = 0; j < game_nb_cols(g); j++) {
            if (game_get_square(g, i, j) == 1) {
                if (left_lighted(g, i, j) || right_lighted(g, i, j) || up_lighted(g, i, j) || down_lighted(g, i, j)) {
                    return false;
                }
            }
        }
    }
    return true;
}

//-------------------------------------------------------------------------------------------------------------------------//
//                                                 RULE 3                                                                  //
//-------------------------------------------------------------------------------------------------------------------------//

bool walls_constraints_check(game g) {             
    assert(g);
    for (int i = 0; i < game_nb_rows(g); i++) {
        for (int j = 0; j < game_nb_cols(g); j++) {
            if (game_is_black(g, i, j)) {
                int cpt = 0;
                int goal = game_get_black_number(g, i, j);
                if (goal != -1) {
                    if (j - 1 >= 0 && game_get_square(g, i, j - 1) == 1) {
                        cpt++;
                    }
                    if (j + 1 < game_nb_cols(g) && game_get_square(g, i, j + 1) == 1) {
                        cpt++;
                    }
                    if (i - 1 >= 0 && game_get_square(g, i - 1, j) == 1) {
                        cpt++;
                    }
                    if (i + 1 < game_nb_rows(g) && game_get_square(g, i + 1, j) == 1) {
                        cpt++;
                    }
                    if (cpt != goal) {
                        return false;
                    }
                }
            }
        }
    }
    return true;
}


bool verifier(game g)
{
    /*Implement here the verifier that the game is solved. You may want to cut this function into subfunctions.*/
    return (no_lights_aligned(g) && walls_constraints_check(g) && all_cells_lighted(g));
}

uint rec_pow_of_two(uint power) {
    if (power == 0) {
        return 1;
    }
    else {
        return (2 * rec_pow_of_two(power - 1));
    }
}


void to_binary(int nb, game g) {
    for (int i = 0; i < game_nb_rows(g); i++) {
        for (int j = 0; j < game_nb_cols(g); j++) {
            if (game_is_blank(g, i, j)) {
                game_set_square(g, i, j, nb % 2);
            }
            nb = nb / 2;
            if (nb == 0) {
                return;
            }
        }
    }
}



bool brute_force(game g)
{
    /*Implement here a brute force algorithm, calling verifier as a subroutine. You may (should) define an auxiliary function that is recursive to perform the choices.
    Make it as efficient as possible, while being correct.*/
    if (verifier(g)) {
        printf("game solution is :\n");
        game_print(g, true);
        return true;
    }
    game g2 = game_copy(g);
    game g3 = game_copy(g);         // jeu "tampon" qu'on utilisera pour ne pas toucher aux lampes déjà posées
    assert(g2);
    uint cpt = 1;
    uint square_nb = game_nb_cols(g) * game_nb_rows(g);
    uint pow = rec_pow_of_two(square_nb) - 1;
    while (cpt < pow) {
        to_binary(cpt, g2);
        if (verifier(g2)) {
            printf("game solution is :\n");
            game_print(g2, true);
            game_delete(g2);
            printf("solutions tested : %u\n", cpt);
            return true;
        }
        cpt ++;
        g2 = game_copy(g3);
    }
    game_delete(g2);
    return false;
}
