import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * The SimpleCannibals class implements a simple uninformed search to solve the
 * well-known missionaries and cannibals problem.
 * 
 * @author jmac
 * @version August 2014
 */
public class SimpleCannibals {

	/** the maximum number of people who can be in the boat */
	public static final int BOAT_SIZE = 2;

	/** the names of the river banks */
	public enum BANK {
		Left, Right
	};

	// a State object tells us how many cannibals and missionaries are on each
	// bank, and where the boat is.
	private class State {
		private int left_missionaries;
		private int left_cannibals;
		private int right_missionaries;
		private int right_cannibals;
		private BANK boat_location;

		// construct a new state with the given parameters
		private State(int left_missionaries, int left_cannibals,
				int right_missionaries, int right_cannibals, BANK boat_location) {
			this.left_missionaries = left_missionaries;
			this.left_cannibals = left_cannibals;
			this.right_missionaries = right_missionaries;
			this.right_cannibals = right_cannibals;
			this.boat_location = boat_location;
		}

		// return true if the state is valid -- that is, if the missionaries
		// will not be eaten.
		private boolean isValid() {
			// will be missionaries on the left bank be eaten?
			if (left_missionaries > 0 && left_missionaries < left_cannibals) {
				return false;
			}
			// will be missionaries on the right bank be eaten?
			if (right_missionaries > 0 && right_missionaries < right_cannibals) {
				return false;
			}
			return true;
		}

		// return true if this state is a goal state -- that is, everyone has
		// made it to the right bank
		private boolean isGoal() {
			return (left_missionaries == 0 && left_cannibals == 0);
		}

		// transfer the given number of missionaries and cannibals from the
		// boat's current location to the other bank
		private void transfer(int num_missionaries, int num_cannibals) {
			if (boat_location == BANK.Left) {
				left_missionaries -= num_missionaries;
				left_cannibals -= num_cannibals;
				right_missionaries += num_missionaries;
				right_cannibals += num_cannibals;
				boat_location = BANK.Right;
			} else {
				left_missionaries += num_missionaries;
				left_cannibals += num_cannibals;
				right_missionaries -= num_missionaries;
				right_cannibals -= num_cannibals;
				boat_location = BANK.Left;
			}
		}

		// create a deep copy of the current state and return it
		private State copy() {
			return new State(left_missionaries, left_cannibals,
					right_missionaries, right_cannibals, boat_location);
		}

		// Return a new state applying the given action to the current state.
		// The current state is unaffected
		private State applyAction(Action action) {
			State new_state = this.copy();
			new_state.transfer(action.num_missionaries, action.num_cannibals);
			return new_state;
		}

		// return true if applying the given action to the current state would
		// result in a valid state
		private boolean willBeValid(Action action) {
			return this.applyAction(action).isValid();
		}

		// return a collection of all actions that result in a valid state when
		// applied to the current state
		private Collection<Action> getValidActions() {
			Collection<Action> valid_actions = new LinkedList<Action>();
			// the maximum number of missionaries who can be transported
			int max_missionaries;
			if (boat_location == BANK.Left) {
				max_missionaries = left_missionaries;
			} else {
				max_missionaries = right_missionaries;
			}
			max_missionaries = Math.min(BOAT_SIZE, max_missionaries);

			// the maximum number of cannibals who can be transported
			int max_cannibals;
			if (boat_location == BANK.Left) {
				max_cannibals = left_cannibals;
			} else {
				max_cannibals = right_cannibals;
			}
			max_cannibals = Math.min(BOAT_SIZE, max_cannibals);

			// iterate over all possible numbers of missionaries and cannibals
			// to be transferred
			for (int num_missionaries = 0; num_missionaries <= max_missionaries; num_missionaries++) {
				for (int num_cannibals = 0; num_cannibals <= max_cannibals; num_cannibals++) {
					// someone has to drive the boat!
					if (num_cannibals == 0 && num_missionaries == 0) {
						continue;
					}
					// there can't be too many people in the boat
					if (num_cannibals + num_missionaries > BOAT_SIZE) {
						continue;
					}
					// we have a potentially plausible number of missionaries
					// and cannibals, so create the corresponding action and
					// see if the results will be valid -- if so, add the action
					// to the list of valid actions
					Action action = new Action(num_missionaries, num_cannibals);
					if (willBeValid(action)) {
						valid_actions.add(action);
					}
				}
			}
			return valid_actions;
		}

		// return a string describing the current state
		@Override
		public String toString() {
			StringBuilder s = new StringBuilder();
			s.append("left: " + left_missionaries + " missionaries, "
					+ left_cannibals + " cannibals");
			if (boat_location == BANK.Left) {
				s.append(", boat");
			}
			s.append("    right: " + right_missionaries + " missionaries, "
					+ right_cannibals + " cannibals");
			if (boat_location == BANK.Right) {
				s.append(", boat");
			}
			return s.toString();
		}

		// automatically generated by eclipse. Needed for use in HashSet<State>
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((boat_location == null) ? 0 : boat_location.hashCode());
			result = prime * result + left_cannibals;
			result = prime * result + left_missionaries;
			result = prime * result + right_cannibals;
			result = prime * result + right_missionaries;
			return result;
		}

		// automatically generated by eclipse. Needed for use in HashSet<State>
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			State other = (State) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (boat_location == null) {
				if (other.boat_location != null)
					return false;
			} else if (!boat_location.equals(other.boat_location))
				return false;
			if (left_cannibals != other.left_cannibals)
				return false;
			if (left_missionaries != other.left_missionaries)
				return false;
			if (right_cannibals != other.right_cannibals)
				return false;
			if (right_missionaries != other.right_missionaries)
				return false;
			return true;
		}

		// automatically generated by eclipse. Needed for use in HashSet<State>
		private SimpleCannibals getOuterType() {
			return SimpleCannibals.this;
		}

	}

	/**
	 * An Action object describes the number of missionaries and cannibals to be
	 * transferred from one bank to the other in the boat.
	 */
	private class Action {
		// the number of missionaries to be transferred
		private int num_missionaries;
		// the number of cannibals to be transferred
		private int num_cannibals;

		// construct a new Action object with the given number of missionaries
		// and cannibals
		private Action(int num_missionaries, int num_cannibals) {
			this.num_missionaries = num_missionaries;
			this.num_cannibals = num_cannibals;
		}

		// Return a string describing this action
		@Override
		public String toString() {
			return "transfer " + num_missionaries + " missionaries, "
					+ num_cannibals + " cannibals";
		}

		// automatically generated by eclipse. Needed for use in HashSet<Action>
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + num_cannibals;
			result = prime * result + num_missionaries;
			return result;
		}

		// automatically generated by eclipse. Needed for use in HashSet<Action>
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Action other = (Action) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (num_cannibals != other.num_cannibals)
				return false;
			if (num_missionaries != other.num_missionaries)
				return false;
			return true;
		}

		// automatically generated by eclipse. Needed for use in HashSet<Action>
		private SimpleCannibals getOuterType() {
			return SimpleCannibals.this;
		}

	}

	/**
	 * A Node object represents a node in the search tree used to find solutions
	 * to the cannibal problem. Apart from the initial node, every node is
	 * reached by applying some action to its parent node.
	 */
	private class Node {
		// the state of the river at this point in the search tree
		private State state;
		// the graph node from which this node was obtained, by applying some
		// action
		private Node parent;
		// the action used to obtain this node; the given action was applied to
		// this node's parent
		private Action action;
		// the depth of this node in the search tree. The initial node has depth
		// 0. Children of the initial node have depth 1, and so on.
		private int depth;

		// Construct a new Node object with the given parameters
		private Node(State state, Node parent, Action action, int depth) {
			this.state = state;
			this.parent = parent;
			this.action = action;
			this.depth = depth;
		}

		// Return a new node in the search tree, obtained by applying the given
		// action to the current node.
		private Node produceChild(Action action) {
			State new_state = state.applyAction(action);
			// by definition, the child node has depth one greater than its
			// parent
			int new_depth = depth + 1;
			Node child = new Node(new_state, this, action, new_depth);
			return child;
		}

		// return a string describing all of the states and actions on the path
		// from the initial node to the calling node
		private String pathDetails() {
			// Step 1: walk up the tree from the current node to the initial
			// node, storing each node encountered on a stack so that the path
			// will be easy to reverse later.
			Stack<Node> path = new Stack<Node>();
			Node current = this;
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
			details.append("Path of depth " + this.depth + '\n');
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

	}

	// A Frontier object is primarily a FIFO queue of search nodes, consisting
	// of the nodes we plan to explore later. However, we need an efficient way
	// of checking whether the state of a given node is already in the queue.
	// Therefore, a Frontier object also maintains a separate set consisting of
	// all states in the queue.
	private class Frontier {
		// the FIFO queue of nodes to be explored later
		private Queue<Node> node_queue = new LinkedList<Node>();
		// the set of states in the queue's nodes
		private Set<State> state_set = new HashSet<State>();

		// add the given node to the frontier
		private void add(Node node) {
			node_queue.add(node);
			state_set.add(node.state);
		}

		// remove and return the node at the head of the frontier's queue
		private Node remove() {
			Node node = node_queue.poll();
			state_set.remove(node.state);
			return node;
		}

		// return true if the frontier contains the given state
		private boolean contains(State state) {
			return state_set.contains(state);
		}

		// return the number of nodes in the frontier
		private int size() {
			return node_queue.size();
		}
	}

	// Return a string describing the solution to the missionaries and cannibals
	// problem, assuming there are num_cannibals cannibals, and also
	// num_cannibals missionaries. If no solution exists, the string
	// "no solution" is returned.
	// This method uses a standard uninformed search strategy, based closely on
	// the recommended implementation in Russell and Norvig.
	// Easy challenge: can you determine which search strategy is being used
	// here?
	private String findSolution(int num_cannibals) {
		// our search for a solution starts from the prescribed initial state
		State initial_state = new State(num_cannibals, num_cannibals, 0, 0,
				BANK.Left);
		Node child = new Node(initial_state, null, null, 0);

		// this is a technicality -- we need to check if the initial state is
		// already a goal state, in which case there is no work to do
		if (child.state.isGoal()) {
			return child.pathDetails();
		}

		// create the frontier (which is a FIFO queue of nodes to be explored
		// later), initializing it to contain the initial node
		Frontier frontier = new Frontier();
		frontier.add(child);

		// create the set of already-explored states, which is initially empty
		Set<State> explored = new HashSet<State>();

		// enter a loop that, in each iteration, examines the head node of the
		// frontier. If we have reached our goal we can return the solution. If
		// not, all possible children (except those whose states have been
		// explored already, or are in the frontier already)
		// need to be added to the frontier for exploration later.
		while (true) {
			if (frontier.size() == 0) {
				return "no solution";
			}
			Node node = frontier.remove();
			explored.add(node.state);
			for (Action action : node.state.getValidActions()) {
				// 5-10 lines of code have been omitted here.
				// challenge: fill in the missing code
				child = node.produceChild(action);
				if (child.state.isGoal()) {
					return child.pathDetails();
				} 
				else if (!frontier.contains(child.state) && !explored.contains(child.state)) {
					frontier.add(child);
				}
			}
		}

	}

	/**
	 * Computes a solution to the missionaries and cannibals problem. No
	 * arguments are required.
	 * 
	 * @param args
	 *            no arguments are acquired
	 */
	public static void main(String[] args) {
		SimpleCannibals cannibals = new SimpleCannibals();
		String solution = cannibals.findSolution(3);
		System.out.println(solution);
	}

}
