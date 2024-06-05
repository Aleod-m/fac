from chess import Board
"""
Heuristic that returns a positive score if m has a better position and negative otherwise.
"""
def heuristic(board: Board, color: bool):
    value = 0
    board_state = board.piece_map()
    for (sq, piece) in list(board_state.items()):
        sign = 1 if piece.color else -1
        symbol = piece.symbol()
        match symbol.lower():
            case 'k':
                value += sign * 200
            case 'q':
                value += sign * 9
            case 'r':
                value += sign * 5
            case 'b'| 'n':
                value += sign * 3
            case 'p':
                pawn_max_score = 2
                wob = (sign + 1) // 2 # is it a blac or white piece
                row = sq // 8
                value += wob * white_pawn_score(row, pawn_max_score) # if piece is white add the pawn score
                value -= (1 - wob) * black_pawn_score(row, pawn_max_score) # if piece is black is substract score
            case _:
                continue
    return value if color else -value

"""
Helper function that calculate the pawn value. Its maximum value 
"""
def white_pawn_score(row: int, pawn_max_score: float):
    return ((pawn_max_score - 1) * row + (6 - pawn_max_score)) * .2
def black_pawn_score(row: int, pawn_max_score: float):
    return ((1 - pawn_max_score) * row + (6*pawn_max_score-1)) * .2


if __name__=="__main__":
    board = Board()
    print(board)
    print(f"heuristic {heuristic(board, True)}")