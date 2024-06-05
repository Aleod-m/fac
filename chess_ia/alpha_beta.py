from random import choice
from heuristic import heuristic as h
from chess import Board, Move

def _nega_ab(board: Board, alpha: float, beta: float, depth: int, turn:bool) -> float:
    if depth == 0:
        return h(board, turn)
    elif board.is_game_over():
        match board.result():
            case "1/2-1/2":
                return -100
            case _:
                return float("inf")
    else:
        for move in board.legal_moves:
            board.push(move)
            value = -_nega_ab(board, -beta, -alpha, depth-1, turn)
            board.pop()
            if value > alpha:
                alpha = value
                if alpha > beta:
                    return alpha

        return alpha

def find_best_move(board: Board, depth: int) -> Move:
    moves = list(board.legal_moves)
    vals = []
    turn = board.turn
    for move in moves:
        board.push(move)
        vals.append(_nega_ab(board, float("-inf"), float("inf"), depth, turn))
        board.pop()
    c = choice(list(filter(lambda a: a == max(vals), vals)))
    return moves[vals.index(c)]

def alphabeta_player(depth: int):
    return lambda board: find_best_move(board, depth)