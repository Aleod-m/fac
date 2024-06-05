import time
import chess
from random import randint, choice

board = chess.Board()
depth = 1
success = True

def search(board, depth):
    if depth == 0:
        return
    lm = board.generate_legal_moves()
    for move in lm:
        board.push(move)
        search(board, depth-1)
        board.pop()

while success:
    t = time.time()
    search(board, depth)
    success = (time.time() - t) < 30
    depth += 1
depth -= 1

print(f"The search reached the depth {depth}")