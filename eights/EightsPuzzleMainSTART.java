import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;



/**
 * EightsPuzzleMain is a class containing only static methods, useful for
 * running searches on 8-puzzle-like problems. A main method is included for
 * running these searches from the command line.
 * 
 * @author Michael Skalak, Dickinson College
 * @version September 2018
 */
public class EightsPuzzleMain {

	/**
	 * The initial configuration of the puzzle board.
	 */
	static public int[][] START_BOARD;


	/**
	 * Runs a search on an 8-puzzle (or similar), printing some details about
	 * the solution.
	 * 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		//////////////////////////////////////////////////////////////////
		// Set some parameters 
		//////////////////////////////////////////////////////////////////
		
		
		
		

		//////////////////////////////////////////////////////////////////
		// Process input arguments
		//////////////////////////////////////////////////////////////////
		
		Scanner sc = new Scanner(new File("input.txt"));

		String searchAlgorithm = sc.nextLine();

		String searchTypeString = sc.nextLine();
		int maxNodes = sc.nextInt();
		int maxDepth = sc.nextInt();
		int numRows = sc.nextInt();
		int numCols = sc.nextInt();
		START_BOARD = new int[numRows][numCols];
		// The puzzle board to be used as a goal.
		int[][] goal = new int[numRows][numCols];
		int curStart = 0;
		for(int i=  0; i <numRows; ++i) {
			for(int j = 0; j < numCols; ++j) {
				goal[i][j] = sc.nextInt();
				START_BOARD[i][j] = curStart++;
			}
		}
		ClassicalSearch.SearchType searchType = ClassicalSearch.SearchType.Tree;
		EightsPuzzleWorldState initial_state = new EightsPuzzleWorldState(
				START_BOARD);
		SearchNode initial_node = null;

		if (searchAlgorithm.equals("bfs")) {
			initial_node = new BreadthFirstSearchNode(null, initial_state, null);
		} else if (searchAlgorithm.equals("dfs")) {
			throw new RuntimeException("depth first search not implemented yet");
		} else if (searchAlgorithm.equals("as1")) {
			throw new RuntimeException("A* search with heuristic h1 not implemented yet");
		} else if (searchAlgorithm.equals("as2")) {
			throw new RuntimeException("A* search with heuristic h2 not implemented yet");
		} else if (searchAlgorithm.equals("ids")) {
			throw new RuntimeException("iterative deepening search not implemented yet");
		} else {
			
		}

		if (searchTypeString.equals("tree")) {
			searchType = ClassicalSearch.SearchType.Tree;
		} else  {
			searchType = ClassicalSearch.SearchType.Graph;
		} 


		EightsPuzzleWorldState goal_state = new EightsPuzzleWorldState(goal);

		//////////////////////////////////////////////////////////////////
		// Run the search.
		//////////////////////////////////////////////////////////////////
		
		ClassicalSearch classical_search = new ClassicalSearch(initial_node,
				goal_state, maxNodes, maxDepth, searchType);

		if (classical_search.search()) {
			System.out.println("Solution found.");
		} else {
			System.out.println("No solution found.");
		}
		System.out.println("Expanded nodes: "
				+ classical_search.getExpandedNodes());
		System.out.println("Generated nodes: "
				+ classical_search.getGeneratedNodes());
	}

}


class ClassicalSearch {
	private static final boolean VERBOSE = false;

	/**
	 * Specifies whether tree search or graph search is used.
	 */
	public enum SearchType {
		Tree, Graph
	};

	// the root node of the search tree
	private SearchNode initialNode;

	// the state that is the goal of the search
	private EightsPuzzleWorldState goalState;

	// the maximum number of nodes to be expanded before the search terminates
	// in failure (-1 means no maximum)
	private int maxNodes;

	// the maximum depth of nodes to be examined (-1 means no maximum)
	private int maxDepth;

	// the type of search to be performed
	private SearchType searchType;

	// a node in the search tree that represents a solution, or null if a
	// solution has not been found
	private SearchNode solutionNode = null;

	// the number of nodes that have been expanded so far in the search
	private int expandedNodes;

	// the number of nodes that have been generated so far in the search
	private int generatedNodes;

	/**
	 * Creates a new ClassicalSearch object in order to perform a classical
	 * search algorithm.
	 * 
	 * @param initialNode
	 *            The root node of the search tree.
	 * @param goalState
	 *            The state that is the goal of the search.
	 * @param maxNodes
	 *            The maximum number of nodes to be expanded before the search
	 *            terminates in failure, or -1 for no maximum
	 * @param maxDepth
	 *            The maximum depth of nodes to be examined, or -1 for no maximum
	 * @param searchType
	 *            The type of search to be performed
	 */
	public ClassicalSearch(SearchNode initialNode, EightsPuzzleWorldState goalState,
			int maxNodes, int maxDepth, SearchType searchType) {
		this.initialNode = initialNode;
		this.goalState = goalState;
		this.maxNodes = maxNodes;
		this.maxDepth = maxDepth;
		this.searchType = searchType;
		this.generatedNodes = 1;
		this.expandedNodes = 0;
	}

	/**
	 * Returns a node in the search tree that represents a solution.
	 * 
	 * @return A node in the search tree that represents a solution, or null if
	 *         a solution has not been found.
	 */
	public SearchNode getSolutionNode() {
		return solutionNode;
	}

	/**
	 * Returns the number of nodes that have been expanded.
	 * 
	 * @return The number of nodes that have been expanded.
	 */
	public int getExpandedNodes() {
		return expandedNodes;
	}

	/**
	 * Returns the number of nodes that have been generated.
	 * 
	 * @return The number of nodes that have been generated.
	 */
	public int getGeneratedNodes() {
		return generatedNodes;
	}

	/**
	 * Perform the classical state space search defined by this object.
	 * 
	 * @return true if a solution is found and false otherwise.
	 */
	public boolean search() {
		// Stores the nodes that have been generated, but not yet expanded. The
		// nodes are ordered according to the cost of their states.
		PriorityQueue<SearchNode> frontier = null;

		// Place the initial node into the frontier.
		frontier = new PriorityQueue<SearchNode>();
		frontier.add(initialNode);

		// As long as we haven't found a solution and there are still nodes in
		// the frontier, continue the search.
		while (!frontier.isEmpty() && solutionNode == null) {

			// Get the lowest cost node from the priority queue.
			SearchNode currentNode = frontier.poll();

			if (VERBOSE) {
				System.out.println("Considering Node:");
				currentNode.print();
			}

			// If the node about to be expanded has the goal state then we have
			// found a solution, so break out of the loop.
			if (currentNode.getState().equals(goalState)) {
				solutionNode = currentNode;
				if (VERBOSE) {
					System.out.println("Solution found");
				}
				break;
			}

			// Expand the current node to get all of the child states that can
			// be reached by application of valid actions.
			Collection<SearchNode> childNodes = currentNode.expand();

			if (VERBOSE) {
				System.out.println("Expanding into ...");
			}

			// Put each of the child nodes into the frontier for consideration
			// later.
			for (SearchNode child : childNodes) {
				frontier.add(child);

				if (VERBOSE) {
					child.print();
					System.out.println("Added to frontier. (cost "
							+ child.getCost() + ")\n\n");
				}
			}
		}

		return solutionNode != null;
	}

}

class EightsPuzzleWorldState  {

	/**
	 * A fixed value used to represent the presence of the gap, or "hole", in
	 * the puzzle board.
	 */
	public static final int HOLE_VALUE = 0;

	// A two-dimensional array representing the numbers on
	// the tiles in the
	// puzzle board.
	private int[][] board;

	// The number of rows and columns in the puzzle board,
	// respectively.
	private int boardHeight;
	private int boardWidth;

	// The row and column of the current location of the
	// hole. Thus, we must
	// maintain the invariant that
	// board[holeRow][holeColumn] == HOLE_VALUE.
	private int holeRow;
	private int holeColumn;

	/**
	 * Create a new puzzle state representing the given puzzle board.
	 * 
	 * @param board
	 *            An array representing the numbers on the tiles in the puzzle
	 *            board.
	 */
	public EightsPuzzleWorldState(int[][] board) {
		this.board = board;
		this.boardHeight = board.length;
		this.boardWidth = board[0].length;
		// initialize the holeRow, holeColumn fields
		// correctly
		computeHoleLocation();
	}

	// Set the values of the holeRow, holeColumn fields to maintain the
	// invariant that board[holeRow][holeColumn] == HOLE_VALUE.
	private void computeHoleLocation() {
		boolean foundHole = false;
		for (int i = 0; i < boardHeight; i++) {
			for (int j = 0; j < boardWidth; j++) {
				if (board[i][j] == HOLE_VALUE) {
					holeRow = i;
					holeColumn = j;
					foundHole = true;
				}
			}
		}
		if (!foundHole) {
			throw new RuntimeException("No hole found in the puzzle.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see WorldState#hashCode()
	 */
	@Override
	public int hashCode() {
		// Arrays.deepHashCode() computes a hash of all elements in a 2-D array,
		// which is what we need here.
		return Arrays.deepHashCode(board);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see WorldState#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		EightsPuzzleWorldState other = (EightsPuzzleWorldState) obj;
		// Arrays.deepEquals() tests for the equality of all elements in a 2-D
		// array, which is what we need here.
		
		return Arrays.deepEquals(board, other.board);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see WorldState#getValidActions()
	 */
	public ArrayList<EightsPuzzleAction> getValidActions() {
		ArrayList<EightsPuzzleAction> actions = new ArrayList<EightsPuzzleAction>();

		// Can we slide up? If so, add the relevant action.
		if (holeRow < boardHeight - 1) {
			actions.add(new EightsPuzzleAction(EightsPuzzleAction.Direction.Up));
		}

		// TODO: fill in the missing code
		
		return actions;
	}

	/**
	 * Clones the puzzle board.
	 * 
	 * @return A new 2-D array containing a copy of the calling object's puzzle
	 *         board.
	 */
	public int[][] cloneBoard() {
		int[][] tmp = board.clone();
		for (int row = 0; row < board.length; row++) {
			tmp[row] = board[row].clone();
		}
		return tmp;
	}

	/* (non-Javadoc)
	 * @see WorldState#apply(Action)
	 */
	public EightsPuzzleWorldState apply(EightsPuzzleAction action) {
		EightsPuzzleAction theAction = (EightsPuzzleAction) action;
		int[][] newBoard = cloneBoard();
		int tileToMove = -1;
		int newHoleRow = holeRow;
		int newHoleColumn = holeColumn;

		switch (theAction.getDirection()) {
		case Up:
			newHoleRow++;
			break;

	    // TODO: fill in the missing code
			
		}

		tileToMove = board[newHoleRow][newHoleColumn];
		newBoard[holeRow][holeColumn] = tileToMove;
		newBoard[newHoleRow][newHoleColumn] = HOLE_VALUE;

		return new EightsPuzzleWorldState(newBoard);
	}

	/* (non-Javadoc)
	 * @see WorldState#toString()
	 */
	@Override
	public String toString() {
		StringBuilder representation = new StringBuilder();
		for (int row = 0; row < boardHeight; row++) {
			for (int col = 0; col < boardWidth; col++) {
				if (board[row][col] == HOLE_VALUE) {
					representation.append("   ");
				} else {
					representation.append(" " + board[row][col] + " ");
				}
			}
			representation.append("\n");
		}
		return representation.toString();
	}

	/**
	 * Return a reference to the calling object's puzzle board.
	 * @return A reference to the calling object's puzzle board.
	 */
	public int[][] getBoard() {
		return board;
	}

}

class EightsPuzzleAction {

	/**
	 * The directions in which tiles can be moved.
	 */
	public enum Direction {
		Up, Down, Left, Right
	};

	// The direction in which a tile is moved to perform this action.
	private Direction direction;

	/**
	 * Create a new EightsPuzzleAction representing a move in a given direction.
	 * 
	 * @param direction
	 *            The direction in which a tile is moved to perform this action.
	 */
	public EightsPuzzleAction(Direction direction) {
		super();
		this.direction = direction;
	}

	/** Returns the direction in which a tile is moved to perform this action.
	 * @return The direction in which a tile is moved to perform this action.
	 */
	public Direction getDirection() {
		return direction;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return direction.toString();
	}

}


abstract class SearchNode implements Comparable<SearchNode> {
	// The cost of this node. It is initialized to NaN so that we can check
	// whether derived classes have correctly initialized the cost field in
	// their constructors.
	protected double cost = Double.NaN;

	// The parent of this node in the search tree, or null if this node is the
	// root of the tree.
	protected SearchNode parent;

	// The state of the world that this node represents.
	protected EightsPuzzleWorldState state;

	// The action that was used to transition from the parent node to this node,
	// or null if this node is the root of the tree.
	protected EightsPuzzleAction action;

	/**
	 * Creates a new node with the given parent, state, and action.
	 * 
	 * @param parent
	 *            The parent of this node in the search tree, or null if this
	 *            node is the root of the tree.
	 * @param state
	 *            The state of the world that this node represents.
	 * @param action
	 *            The action that was used to transition from the parent node to
	 *            this node, or null if this node is the root of the tree.
	 */
	public SearchNode(SearchNode parent, EightsPuzzleWorldState state, EightsPuzzleAction action) {
		this.parent = parent;
		this.state = state;
		this.action = action;
	}

	/**
	 * Returns the cost of this node, or throws a runtime exception if the cost
	 * was not initialized correctly in the constructor when the node was
	 * created.
	 * 
	 * @return The cost of this node.
	 */
	public double getCost() {
		if (Double.isNaN(cost)) {
			throw new RuntimeException(
					"error: cost should have been computed in constructor of "
							+ this.getClass() + "class");
		}
		return cost;
	}

	/**
	 * Returns the parent of this node.
	 * 
	 * @return The parent of this node.
	 */
	public SearchNode getParent() {
		return parent;
	}

	/**
	 * Returns the state of the world represented by this node.
	 * 
	 * @return The state of the world represented by this node.
	 */
	public EightsPuzzleWorldState getState() {
		return state;
	}

	/**
	 * We want to be able to use SearchNode objects as the elements of a
	 * PriorityQueue, so we must override the compareTo method, specifying that
	 * lower-cost nodes are "less" than higher-cost nodes.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(SearchNode other) {
		return ((Double) getCost()).compareTo(other.getCost());
	}

	/**
	 * Prints out a human-readable description of this node.
	 */
	public void print() {
		System.out.println(this.toString());
	}

	/**
	 * Expands this node, returning a collection of all nodes generated by
	 * applying valid actions to the current node.
	 * 
	 * @return A collection of nodes that can be reached by applying actions to
	 *         the current node.
	 */
	public Collection<SearchNode> expand() {
		Collection<EightsPuzzleAction> actions = state.getValidActions();
		ArrayList<SearchNode> children = new ArrayList<SearchNode>();
		for (EightsPuzzleAction action : actions) {
			EightsPuzzleWorldState childState = state.apply(action);
			SearchNode childNode = createChild(childState, action);
			children.add(childNode);
		}
		return children;
	}

	// Create a new node, which is a child of the calling node, and which has
	// the given state and action.
	//
	// Returns: The newly-constructed child node.
	// Param childState: The state to be assigned to the new child node.
	// Param action: The action to be assigned to the new child node.
	protected abstract SearchNode createChild(EightsPuzzleWorldState childState,
			EightsPuzzleAction action);

	// return a string describing all of the states and actions on the path
	// from the initial node to the calling node

	/**
	 * Returns a string describing the path from the initial node to the calling
	 * node.
	 * 
	 * @return A string describing all of the states and actions on the path
	 *         from the initial node to the calling node.
	 */
	public String pathDetails() {
		// Step 1: walk up the tree from the current node to the initial
		// node, storing each node encountered on a stack so that the path
		// will be easy to reverse later.
		Stack<SearchNode> path = new Stack<SearchNode>();
		SearchNode current = this;
		path.push(current);
		while (current.parent != null) {
			current = current.parent;
			path.push(current);
		}
		// Step 2: the top of the stack is the initial node, so by popping
		// nodes off the stack one by one, and appending their details to a
		// string, we can build up a complete description of the path from
		// the initial node to the calling node.
		StringBuilder details = new StringBuilder();
		while (!path.empty()) {
			current = path.pop();
			// unless we are at the initial node, the action should be
			// non-null and we should append its details to the string
			if (current.action != null) {
				details.append(current.action.toString() + '\n');
			}
			// append details of this state to the string also
			details.append(current.state.toString() + '\n');
		}
		return details.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// We choose to only describe the state here, since more details can be
		// obtained from the pathDetails() method if necessary.
		return state.toString();
	}

}

class BreadthFirstSearchNode extends SearchNode {
	// Keep track of the total number of nodes created, as this will be used for
	// assigning the cost of a node.
	private static int numCreated = 0;

	/**
	 * @param parent
	 * @param state
	 * @param action
	 */
	public BreadthFirstSearchNode(SearchNode parent, EightsPuzzleWorldState state,
			EightsPuzzleAction action) {
		super(parent, state, action);
		numCreated++;
		cost = numCreated;
	}

	@Override
	protected SearchNode createChild(EightsPuzzleWorldState childState, EightsPuzzleAction action) {
		return new BreadthFirstSearchNode(this, childState, action);
	}

}