from random import choice
from heuristic import heuristic as h
from chess import Board

def _nega_minmax(board: Board, depth:int, turn: bool) -> float:
    if depth == 0:
        return h(board, turn)
    elif board.is_game_over():
        match board.result():
            case "1/2-1/2":
                return 0.
            case _:
                return float("inf")
    else:
        val: float = float("-inf")
        for move in board.legal_moves:
            board.push(move)
            val = max(val, -_nega_minmax(board, depth - 1, turn))
            board.pop()
        return val

def find_best_move(board: Board, depth: int):
    moves = list(board.legal_moves)
    vals = []
    turn = board.turn
    for move in moves:
        board.push(move)
        vals.append(_nega_minmax(board, depth, turn))
        board.pop()
    c = choice(list(filter(lambda a: a == max(vals), vals)))
    return moves[vals.index(c)]

def minmax_player(depth: int):
    return lambda board: find_best_move(board, depth)