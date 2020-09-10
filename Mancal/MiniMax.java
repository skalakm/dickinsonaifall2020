/**
 * A few methods that the MiniMax and AlphaBeta players
 * must implement.
 *
 * @author Grant Braught
 * @author Dickinson College
 * @version Oct 9, 2008
 */
public interface MiniMax {
    
    /**
     * The static evaluation function for your search.  This function
     * must be used by your MiniMax and AlphaBeta algorithms for all
     * static evaluations.  It is separated out so that it can be easily
     * altered for grading purposes.
     * 
     * @param state the state to be evaluated.
     * @return an integer score for the value of the state to the max player.
     */
    public double staticEvaluator(GameState state);
    
    /**
     * Get the number of nodes that were generated
     * during the search.
     * 
     * @return the number of nodes generated.
     */
    public int getNodesGenerated();
    
    /**
     * Get the number of static evaluations that were
     * performed during the search.
     * 
     * @return the number of static evaluations performed.
     */
    public int getStaticEvaluations();
    
    /**
     * Get the average branching factor of the nodes that
     * were expanded during the search.  This is to be computed
     * based upon the actual number of children for each node.
     * 
     * @return the average branching factor.
     */
    public double getAveBranchingFactor();
    
    /**
     * Get the effective branching factor of the nodes that were
     * expanded during the search.  This is to be computed based
     * upon the number of children that are explored in the search.
     * Without alpha/beta pruning this number will be equal to the 
     * average branching factor. With alpha/beta pruning it should be
     * significantly smaller.
     * 
     * @return the effective branching factor.
     */
    public double getEffectiveBranchingFactor();
}
