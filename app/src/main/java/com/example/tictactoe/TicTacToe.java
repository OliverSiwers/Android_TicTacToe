package com.example.tictactoe;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class TicTacToe {

    private final MainActivity ma;

    private int nextPlayer = 0;
    private int turn = 0;
    private int boardOccupied = 0b000000000;
    private int boardSymbols = 0b000000000;

    String[] playerSymbols = {"X", "O"};

    // The rows and cols could be done with just one mask each,
    // the row mask shifted 3 bits, and the col mask shifted 1 bit
    // That's probably not worth it though, considering the extra complexity
    final int[] winMasks = {
            0b000000111, /*Row 1*/
            0b000111000, /*Row 2*/
            0b111000000, /*Row 3*/

            0b001001001, /*Col 1*/
            0b010010010, /*Col 2*/
            0b100100100, /*Col 3*/

            0b100010001, /*Diag tl br*/
            0b001010100, /*Diag tr bl*/
    };

    public TicTacToe(MainActivity ma) {
        this.ma = ma;

        ma.setContentView(R.layout.tic_tac_toe);

        playerSymbols[0] = ma.getString(R.string.tictactoe_p0_symbol);
        playerSymbols[1] = ma.getString(R.string.tictactoe_p1_symbol);

        TextView subtitle = ma.findViewById(R.id.tictactoe_subtitle);
        subtitle.setText(ma.getString(R.string.tictactoe_next_player, playerSymbols[nextPlayer]));

        for (int i = 0, k = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++, k++) {
                Button btn = ma.findViewById(ma.getResources().getIdentifier(
                        "btn" + (i + 1) + "_" + (j + 1),
                        "id",
                        ma.getPackageName()));

                int finalK = k;

                btn.setOnClickListener(view -> {
                    turn++;
                    boardOccupied |= 0b1 << finalK;
                    btn.setText(playerSymbols[nextPlayer]);

                    if (nextPlayer == 0) {
                        boardSymbols |= 0b1 << finalK;
                    } else {
                        boardSymbols &= ~(0b1 << finalK);
                    }

                    btn.setClickable(false);

                    if (checkBoard()) {
                        subtitle.setText(ma.getString(R.string.tictactoe_victory, playerSymbols[nextPlayer]));
                    } else {
                        nextPlayer++;
                        nextPlayer %= 2;

                        subtitle.setText(ma.getString(R.string.tictactoe_next_player, playerSymbols[nextPlayer]));
                    }
                });
            }
        }
    }

    private boolean checkBoard() {
        Log.i("TicTacToe", "Occupied: " + String.format("%9s", Integer.toBinaryString(boardOccupied)).replace(' ', '0'));
        Log.i("TicTacToe", "Symbols:  " + String.format("%9s", Integer.toBinaryString(boardSymbols)).replace(' ', '0'));

        if (turn < 5) //Can't win before turn 5, so just return
            return false;

        for (int mask : winMasks) {
            if ((boardOccupied & mask) == mask) {
                if ((boardSymbols & mask) == mask) { //player 1 won
                    Log.i("TicTacToe", "Player 0 Won");
                    return true;
                } else if ((~boardSymbols & mask) == mask) { //player 2 won
                    Log.i("TicTacToe", "Player 1 Won");
                    return true;
                }
            }
        }
        return false;
    }
}
