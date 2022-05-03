import java.util.*;

/**
 * This is the AiPlayer class.  It simulates a minimax player for the max
 * connect four game.
 * The constructor essentially does nothing.
 *
 * @author james spargo
 *
 */

public class AiPlayer
{
    static int depthLimit;

    // sub class to store state and the
    public class State
    {
        public int utility; // store the utilty of being in that state
        public int turn; // store which turn is being processed
        public int playPiece = -1; // store the chosen path
        public int player; // store the player number
        public int depth; // store the depth
        public GameBoard currentGame; // store the game data from each explored state
        public ArrayList<State> successors; // store all valid successors

        public State(GameBoard currentGame)
        {
            this.currentGame = new GameBoard(currentGame.getGameBoard());
            successors = new ArrayList<State>();
        }
        public ArrayList<State> getSuccessors()
        {
            return new ArrayList<State>(successors);
        }
    }


    /**
     * The constructor essentially does nothing except instantiate an
     * AiPlayer object.
     *
     */
    public AiPlayer()
    {
	// nothing to do here
    }

    /**
     * This method plays a piece randomly on the board
     * @param currentGame The GameBoard object that is currently being used to
     * play the game.
     * @return an integer indicating which column the AiPlayer would like
     * to play in.
     */
    public int findRandom( GameBoard currentGame )
    {
    	// start random play code
    	Random randy = new Random();
    	int playChoice = 99;

    	playChoice = randy.nextInt( 7 );

    	while( !currentGame.isValidPlay( playChoice ) )
    	    playChoice = randy.nextInt( 7 );

    	// end random play code

    	return playChoice;
    }

    // method to find best move utilizing minimax and alpha beta pruning
    // as a depth limited version
    public int findBestMove(GameBoard currentGame)
    {
        int playChoice = 0;

        // create the starting state of the game
        State state = new State(currentGame);
        state.turn = currentGame.getCurrentTurn();
        state.player = state.turn;
        state.depth = 1;

        // find best move
        playChoice = MinimaxDecision(state);
        return playChoice;
    }

    private int MinimaxDecision(State state)
    {
        int playChoice = 0;
        int currentBest = -999999;
        // take the min value for the opponent move
        minValue(state, -999999, 999999);


        // for each possible move, get the one with the best utility
        for( int i = 0; i < state.successors.size(); i++)
        {
            /*
            System.out.println("i: "+i+" u: "+state.successors.get(i).utility+
                " play: "+state.successors.get(i).playPiece+
                " sizeOfSuccessors: "+state.successors.size());
                */
            if( state.successors.get(i).utility > currentBest)
            {
                // store the choice to compare to later state in successors
                currentBest = state.successors.get(i).utility;
                playChoice = state.successors.get(i).playPiece;

            }
        }
        //System.out.println(state.playPiece);
        //playChoice = state.playPiece;
        return playChoice;
    }
    private int maxValue(State state, int alpha, int beta)
    {
        int otherTurn = 0;
        if(state.turn == 1)
        {
            otherTurn = 2;
        }
        else
        {
            otherTurn = 1;
        }

        // terminal test
        // 42 is max move count, or use depth limit
        if(state.currentGame.getPieceCount() == 42 || state.depth >= AiPlayer.depthLimit)
        {
            int opponent = 0;
            if(state.player == 1)
            {
                opponent = 2;
            }
            else
            {
                opponent = 1;
            }

            int score = utilityFunction(state);
            state.utility = score;
            return score;
        }

        // use v to compare if increasing
        // else use v
        int v = -999999;
        int val;
        for(int i = 0; i < 7; i++)
        {
            // get all valid successors
            if(state.currentGame.isValidPlay(i))
            {
                // if valid play create a new state with that move
                // the new state will repeat this until
                State newState = new State(state.currentGame);
                newState.playPiece = i;
                newState.currentGame.playPiece(i);
                newState.depth = state.depth+1;
                newState.player = state.player;
                newState.turn = otherTurn;


                //System.out.println(state.depth +" min "+state.successors.size());

                // find the mininum value for new state's successors
                // this means it find what it evaluates to
                // be the best move for opponent
                val = minValue(newState, alpha, beta);
                newState.utility = val;
                newState.playPiece = i;

                // add the new state to this states successors
                state.successors.add(newState);

                // alpha beta pruning
                // and get greatest val
                if(val > v)
                {
                    v = val;

                    if(v >= beta)
                    {
                        return v;
                    }
                    if(alpha < v)
                    {
                        alpha = v;
                    }
                }
            }
        }
        return v;
    }
    private int minValue(State state, int alpha, int beta)
    {
        int otherTurn = 0;
        if(state.turn == 1)
        {
            otherTurn = 2;
        }
        else
        {
            otherTurn = 1;
        }

        //terminal test
        if(state.currentGame.getPieceCount() == 42 || state.depth >= AiPlayer.depthLimit)
        {
            int opponent = 0;
            if(state.player == 1)
            {
                opponent = 2;
            }
            else
            {
                opponent = 1;
            }

            int score = utilityFunction(state);
            state.utility = score;
            return score;
        }

        int v = 999999;
        int val;
        for(int i = 0; i < 7; i++)
        {
            // get all valid successors
            if(state.currentGame.isValidPlay(i))
            {
                // if valid play create a new state with that move
                // the new state will repeat this until
                State newState = new State(state.currentGame);
                newState.playPiece = i;
                newState.currentGame.playPiece(i);
                newState.depth = state.depth+1;
                newState.player = state.player;
                newState.turn = otherTurn;

                //System.out.println(state.depth +" min "+ state.successors.size());

                // find the maximunm value for new state's successors
                // this means it find what it evaluates to
                // be the best move for itself
                val = maxValue(newState, alpha, beta);
                newState.utility = val;
                newState.playPiece = i;

                // add the new state to this states successors
                state.successors.add(newState);

                // alpha beta pruning
                // and get lowest val
                if(val < v)
                {
                    v = val;

                    if(v <= alpha)
                    {
                        return v;
                    }
                    if(beta > v)
                    {
                        beta = v;
                    }
                }
            }
        }
        return v;
    }
    private int utilityFunction(State state)
    {
        int utility = 0;
        int opponent = 0;
        if(state.player == 1)
        {
            opponent = 2;
        }
        else
        {
            opponent = 1;
        }
        int tempScore = state.currentGame.getScore(state.player);
        utility += (tempScore * 10);
        tempScore = state.currentGame.getScore(opponent);
        utility -= (tempScore * 10);

        // check for close to 4s and give a little utility

        //check horizontally
        for( int i = 0; i < 6; i++ )
            {
            for( int j = 0; j < 4; j++ )
            {
            if( ( state.currentGame.playBoard[ i ][j] == state.player ) &&
                ( state.currentGame.playBoard[ i ][ j+1 ] == state.player ) &&
                ( state.currentGame.playBoard[ i ][ j+2 ] == state.player ) &&
                ( state.currentGame.playBoard[ i ][ j+3 ] == 0 ) )
            {
                utility += 2;
            }
            }
        } // end horizontal

        //check vertically
        for( int i = 0; i < 3; i++ ) {
            for( int j = 0; j < 7; j++ ) {
            if( ( state.currentGame.playBoard[ i ][ j ] == state.player ) &&
                ( state.currentGame.playBoard[ i+1 ][ j ] == state.player ) &&
                ( state.currentGame.playBoard[ i+2 ][ j ] == state.player ) &&
                ( state.currentGame.playBoard[ i+3 ][ j ] == 0 ) ) {
                utility += 2;
            }
            }
        } // end verticle

        //check diagonally - backs lash ->	\
            for( int i = 0; i < 3; i++ ){
            for( int j = 0; j < 4; j++ ) {
                if( ( state.currentGame.playBoard[ i ][ j ] == state.player ) &&
                ( state.currentGame.playBoard[ i+1 ][ j+1 ] == state.player ) &&
                ( state.currentGame.playBoard[ i+2 ][ j+2 ] == state.player ) &&
                ( state.currentGame.playBoard[ i+3 ][ j+3 ] == 0 ) ) {
                utility += 2;
                }
            }
            }

            //check diagonally - forward slash -> /
            for( int i = 0; i < 3; i++ ){
            for( int j = 0; j < 4; j++ ) {
                if( ( state.currentGame.playBoard[ i+3 ][ j ] == state.player ) &&
                ( state.currentGame.playBoard[ i+2 ][ j+1 ] == state.player ) &&
                ( state.currentGame.playBoard[ i+1 ][ j+2 ] == state.player ) &&
                ( state.currentGame.playBoard[ i ][ j+3 ] == 0 ) ) {
                utility += 2;
                }
            }
            }// end player score check
            //check horizontally
        	for( int i = 0; i < 6; i++ )
                {
        	    for( int j = 0; j < 4; j++ )
        	    {
        		if( ( state.currentGame.playBoard[ i ][j] == opponent ) &&
        		    ( state.currentGame.playBoard[ i ][ j+1 ] == opponent ) &&
        		    ( state.currentGame.playBoard[ i ][ j+2 ] == opponent ) &&
        		    ( state.currentGame.playBoard[ i ][ j+3 ] == 0 ) )
        		{
        		    utility -= 2;
        		}
        	    }
        	} // end horizontal

        	//check vertically
        	for( int i = 0; i < 3; i++ ) {
        	    for( int j = 0; j < 7; j++ ) {
        		if( ( state.currentGame.playBoard[ i ][ j ] == opponent ) &&
        		    ( state.currentGame.playBoard[ i+1 ][ j ] == opponent ) &&
        		    ( state.currentGame.playBoard[ i+2 ][ j ] == opponent ) &&
        		    ( state.currentGame.playBoard[ i+3 ][ j ] == 0 ) ) {
        		    utility -= 2;
        		}
        	    }
        	} // end verticle

        	//check diagonally - backs lash ->	\
        	    for( int i = 0; i < 3; i++ ){
        		for( int j = 0; j < 4; j++ ) {
        		    if( ( state.currentGame.playBoard[ i ][ j ] == opponent ) &&
        			( state.currentGame.playBoard[ i+1 ][ j+1 ] == opponent ) &&
        			( state.currentGame.playBoard[ i+2 ][ j+2 ] == opponent ) &&
        			( state.currentGame.playBoard[ i+3 ][ j+3 ] == 0 ) ) {
        			      utility -= 2;
        		    }
        		}
        	    }

        	    //check diagonally - forward slash -> /
        	    for( int i = 0; i < 3; i++ ){
        		for( int j = 0; j < 4; j++ ) {
        		    if( ( state.currentGame.playBoard[ i+3 ][ j ] == opponent ) &&
        			( state.currentGame.playBoard[ i+2 ][ j+1 ] == opponent ) &&
        			( state.currentGame.playBoard[ i+1 ][ j+2 ] == opponent ) &&
        			( state.currentGame.playBoard[ i ][ j+3 ] == 0 ) ) {
        			utility -= 2;
        		    }
        		}
        	    }// end player score check

        //check horizontally
        for( int i = 0; i < 6; i++ )
            {
            for( int j = 0; j < 4; j++ )
            {
            if( ( state.currentGame.playBoard[ i ][j] == 0 ) &&
                ( state.currentGame.playBoard[ i ][ j+1 ] == state.player ) &&
                ( state.currentGame.playBoard[ i ][ j+2 ] == state.player ) &&
                ( state.currentGame.playBoard[ i ][ j+3 ] == state.player ) )
            {
                utility += 2;
            }
            }
        } // end horizontal

        //check vertically
        for( int i = 0; i < 3; i++ ) {
            for( int j = 0; j < 7; j++ ) {
            if( ( state.currentGame.playBoard[ i ][ j ] == 0 ) &&
                ( state.currentGame.playBoard[ i+1 ][ j ] == state.player ) &&
                ( state.currentGame.playBoard[ i+2 ][ j ] == state.player ) &&
                ( state.currentGame.playBoard[ i+3 ][ j ] == state.player ) ) {
                utility += 2;
            }
            }
        } // end verticle

        //check diagonally - backs lash ->	\
            for( int i = 0; i < 3; i++ ){
            for( int j = 0; j < 4; j++ ) {
                if( ( state.currentGame.playBoard[ i ][ j ] == 0 ) &&
                ( state.currentGame.playBoard[ i+1 ][ j+1 ] == state.player ) &&
                ( state.currentGame.playBoard[ i+2 ][ j+2 ] == state.player ) &&
                ( state.currentGame.playBoard[ i+3 ][ j+3 ] == state.player ) ) {
                utility += 2;
                }
            }
            }

            //check diagonally - forward slash -> /
            for( int i = 0; i < 3; i++ ){
            for( int j = 0; j < 4; j++ ) {
                if( ( state.currentGame.playBoard[ i+3 ][ j ] == 0 ) &&
                ( state.currentGame.playBoard[ i+2 ][ j+1 ] == state.player ) &&
                ( state.currentGame.playBoard[ i+1 ][ j+2 ] == state.player ) &&
                ( state.currentGame.playBoard[ i ][ j+3 ] == state.player ) ) {
                utility += 2;
                }
            }
            }// end player score check
            //check horizontally
        	for( int i = 0; i < 6; i++ )
                {
        	    for( int j = 0; j < 4; j++ )
        	    {
        		if( ( state.currentGame.playBoard[ i ][j] == 0 ) &&
        		    ( state.currentGame.playBoard[ i ][ j+1 ] == opponent ) &&
        		    ( state.currentGame.playBoard[ i ][ j+2 ] == opponent ) &&
        		    ( state.currentGame.playBoard[ i ][ j+3 ] == opponent ) )
        		{
        		    utility -= 2;
        		}
        	    }
        	} // end horizontal

        	//check vertically
        	for( int i = 0; i < 3; i++ ) {
        	    for( int j = 0; j < 7; j++ ) {
        		if( ( state.currentGame.playBoard[ i ][ j ] == 0 ) &&
        		    ( state.currentGame.playBoard[ i+1 ][ j ] == opponent ) &&
        		    ( state.currentGame.playBoard[ i+2 ][ j ] == opponent ) &&
        		    ( state.currentGame.playBoard[ i+3 ][ j ] == opponent ) ) {
        		    utility -= 2;
        		}
        	    }
        	} // end verticle

        	//check diagonally - backs lash ->	\
        	    for( int i = 0; i < 3; i++ ){
        		for( int j = 0; j < 4; j++ ) {
        		    if( ( state.currentGame.playBoard[ i ][ j ] == 0 ) &&
        			( state.currentGame.playBoard[ i+1 ][ j+1 ] == opponent ) &&
        			( state.currentGame.playBoard[ i+2 ][ j+2 ] == opponent ) &&
        			( state.currentGame.playBoard[ i+3 ][ j+3 ] == opponent ) ) {
        			utility -= 2;
        		    }
        		}
        	    }

        	    //check diagonally - forward slash -> /
        	    for( int i = 0; i < 3; i++ ){
        		for( int j = 0; j < 4; j++ ) {
        		    if( ( state.currentGame.playBoard[ i+3 ][ j ] == 0 ) &&
        			( state.currentGame.playBoard[ i+2 ][ j+1 ] == opponent ) &&
        			( state.currentGame.playBoard[ i+1 ][ j+2 ] == opponent ) &&
        			( state.currentGame.playBoard[ i ][ j+3 ] == opponent ) ) {
        			utility -= 2;
        		    }
        		}
        	    }// end player score check

        return utility;
    }
}
