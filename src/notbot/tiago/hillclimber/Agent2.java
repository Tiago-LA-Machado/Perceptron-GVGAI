package notbot.tiago.hillclimber;
import java.util.ArrayList;
import java.util.Random;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import ontology.Types.ACTIONS;
import ontology.Types.WINNER;
import tools.ElapsedCpuTimer;

//pseudocode
/*currentNode = startNode;
loop do
L = NEIGHBORS(currentNode);
nextEval = -INF;
nextNode = NULL;
for all x in L 
if (EVAL(x) > nextEval)
nextNode = x;
nextEval = EVAL(x);
if nextEval <= EVAL(currentNode)
//Return current node since no better neighbors exist
return currentNode;
currentNode = nextNode;*/

/**
 * Created with IntelliJ IDEA.
 * User: ssamot
 * Date: 14/11/13
 * Time: 21:45
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class Agent2 extends AbstractPlayer {

    public Agent2(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

    }
    /**
     *
     * Hill Climbing agent.
     * with random node cost
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return An action for the current state
     */
    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
    	
    	ArrayList<Node> queue = new ArrayList<Node>();
    	queue.add(new Node(null, Types.ACTIONS.ACTION_NIL, stateObs));
    	
    	ArrayList<Node> trace = new ArrayList<Node>();
    	
    	float avgTime = 10;
		float worstTime = 10;
		float totalTime = 0;
		int iterations = 0;
		//not used - int numberOfIterations = 0;
		Node currentNode = null;
		ArrayList<Types.ACTIONS> possibleActions = stateObs.getAvailableActions();
		currentNode = queue.remove(0);
		
		while(elapsedTimer.remainingTimeMillis() > 2 * avgTime 
				&& elapsedTimer.remainingTimeMillis() > worstTime)
		{
			if(currentNode.state.getGameWinner() == WINNER.PLAYER_WINS){
				break;
			}
			if(currentNode.state.getGameWinner() == WINNER.PLAYER_LOSES){
				continue;
			}
			
//			if(iterations % 12 == 0)
//			{
//				if(!trace.isEmpty())
//				{
//					Node mutant = doMutation(trace);
//					queue.add(mutant);
//					System.out.println("********************");
//					System.out.println("m_value = " + mutant.value);
//					trace.clear();
//				}
//			}
			
			for (Types.ACTIONS action : possibleActions) 
			{
				StateObservation newState = currentNode.state.copy();
				newState.advance(action);
				queue.add(new Node(currentNode, action, newState));
			}

			if(iterations % 12 == 0 && !trace.isEmpty())
			{
				Node n = sidewalk(trace);
				currentNode = n;
				System.out.println("###############");
			}else{
				currentNode = eval(queue);
			}
			
			System.out.println("value = " + currentNode.value);
			trace.add(currentNode);
			queue.clear();
			iterations = iterations + 1;
			System.out.println(iterations);
		}
		
		return currentNode.getAction();
    }
    
    public boolean getOutLocalOptimum(int exceedLocalOptimum)
    {
    	return true;
    }
    
    public Node sidewalk(ArrayList<Node> trace)
    {
    	Node best = null;
    	double value = Double.MAX_VALUE;
    	for (int i = 0; i < trace.size(); i++) 
    	{
    		if(trace.get(i).value < value)
    		{
    			value = trace.get(i).value;
    			best = trace.get(i);
    		}
		}
    	return best;
    }
    
    public Node doMutation(ArrayList<Node> trace)
    {
    	Random random = new Random();
    	int select = trace.size();
		Node mutant = trace.get(random.nextInt(select));
		mutant.mutation();
		return mutant;
    }
        
    public Node eval(ArrayList<Node> nodes)
	{
		Node node = new Node(null,null,null);
		
		double maxNodeCostInArray = 0;
		
		for (Node n : nodes) 
		{
			if(n.value >= maxNodeCostInArray)
			{
				node = n;
				maxNodeCostInArray = n.value;
			}
		}
		
		for (Node n : nodes) 
		{
			if(n.value >= maxNodeCostInArray)
			{
				node = n;
				maxNodeCostInArray = n.value;
			}
		}
		
		return node;	
	}
    
	public class Node {
		public Node parent;
		public Types.ACTIONS action;
		public StateObservation state;
		public double value;
		
		public Node(Node parent, Types.ACTIONS action, StateObservation state)
		{
			this.parent = parent;
			this.action = action;
			this.state = state;
			this.value = new Random().nextInt(8);
		}
		
		public Types.ACTIONS getAction(){
			if(this.parent == null){
				return action;
			}
			if(this.parent.parent == null){
				return action;
			}
			
			return parent.getAction();
		}
		
		private void mutation()
		{
			this.value = this.value*1.31;
		}
		
//		public double evaluateNodeScore()
//		{
//			this.nodeScore = 0.0;
//			if(this.state != null)
//			{
////				this.nodeScore = 1 + this.state.getAvatarSpeed()
////				+ this.state.getGameScore() + this.state.;
//			}
//		}
		
	}

}