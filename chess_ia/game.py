from chess import Board
from dumb import random_player
from minmax import minmax_player
from alpha_beta import alphabeta_player
from heuristic import heuristic as h

class Player:

    def __init__(self, algo):
        self.algo = algo

    def play(self, board: Board):
        board.push(self.algo(board))


def chose_player_algorithm():
    print("\t1- random")
    print("\t2- minmax")
    print("\t3- alphabeta")
    print(">", end=' ')
    choice: int = int(input())
    match choice:
        case 1:
            return random_player()
        case 2: 
            print("Enter depth: ", end = ' ')
            return minmax_player(int(input()))
        case 3:
            print("Enter depth: ", end = ' ')
            return alphabeta_player(int(input()))
        case _:
             assert False

if __name__ == "__main__":
    #print("Enter a limit of time by moves:", end=' ')
    #time_limit = int(input())
    print("Chose white player algorithm:")
    player1 = Player(chose_player_algorithm())
    print("Chose black player algorithm:")
    player2 = Player(chose_player_algorithm())
    is_over = False
    board = Board()
    
    while True:
        print(h(board, True))
        player1.play(board)
        print(board.unicode(invert_color=True))
        print()
        is_over = board.is_game_over()
        if is_over: break
        player2.play(board)
        print(h(board, True))
        print(board.unicode(invert_color=True))
        print()
        is_over = board.is_game_over()
        if is_over: break
    match board.result():
        case "1/2-1/2":
            print("The game ended in a draw.")
        case '1-0':
            print("White wins the game.")
        case '0-1':
            print("Black wins the game.")

