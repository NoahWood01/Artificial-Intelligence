// Noah Wood
// 1001705291

import java.util.*;

public class bnet 
{
    // create given bayesian network
    /*
        Probabilties:
        Bt = 0.001
        Et = 0.002
        At|Bt and Et = 0.95
        At|Bt and Ef = 0.94
        At|Bf and Et = 0.29
        At|Bf and Ef = 0.001
        Jt|At  = 0.90
        Jt|Af = 0.05
        Mt|At = 0.70
        Mt|Af = 0.01

        Parents
        B - None 
        E - None
        A - B E
        J - A
        M - A
    */
	public static HashMap<String, Double> networkProb = new HashMap<String, Double>();
	public static HashMap<Character, ArrayList<Character>> bayesianNetwork = new HashMap<Character, ArrayList<Character>>();

	public static void main(String[] args) 
    {
        // check input agruments
        for(String arg: args)
        {
            if(!arg.equals("given") && arg.length() > 2)
            {
                System.out.println("Bad input.");
                System.exit(0);
            }
        }
        
		if(args.length < 1 || args.length > 6) 
        {
			System.out.print("Need 1 - 6 arguments\n");
			System.exit(0);
		}

        createNetwork();

		// P (A|B) where A and B are sets
		HashMap<Character, Boolean> A = new HashMap<Character, Boolean>();
		HashMap<Character, Boolean> B = new HashMap<Character, Boolean>();

		// getting values of A and B
		boolean given = false;
        for(String arg : args)
        {
            // once given is true, place remaining in B set
            if(arg.equals("given"))
            {
                given = true;
            }
            else 
            {
                if(arg.charAt(1) == 't')
                {
                    if(!given)
                    {
                        A.put(arg.charAt(0), true);
                    }
                    else 
                    {
                        B.put(arg.charAt(0), true);
                    }
                }
                else 
                {
                    if(!given)
                    {
                        A.put(arg.charAt(0), false);
                    }
                    else 
                    {
                        B.put(arg.charAt(0), false);
                    }
                }
            }
        }

        double probability = -1;
        try
        {
            probability = compute(A, B);
        }
        catch(Exception e)
        {
            System.out.println("Error in input.");
        }
		
		System.out.println("Probability = " + probability);
	}

	public static double compute(HashMap<Character, Boolean> A, HashMap<Character, Boolean> B) 
    {
		// check if sets are in network
        // returns -1 if not found
		double prob = bnetProb(A, B);
		if(prob != -1) 
        {
			return prob;
		}

		double result = 0;
		
        // Use Bayes' Rule
		if(B != null && B.size() != 0) 
        {
			HashMap<Character, Boolean> AB = new HashMap<Character, Boolean>();
			AB.putAll(A);
			AB.putAll(B);
            // recursively call with new sets from Baye's Rule
			result = compute(AB, null);
			result /= compute(B, null);
		} 
        else 
        {
            // check if all baysianNetwork variables are found
            // and get with parent nodes
			if(A.size() == bayesianNetwork.size()) 
            {
				HashMap<Character, Boolean> elementParents = null;
				result = 1;

				for(Map.Entry<Character, Boolean> element : A.entrySet()) 
                {
                    elementParents = null;
                    // get the element's parents
					List<Character> parentNodes = bayesianNetwork.get(element.getKey());
                    if (parentNodes != null) 
                    {
                        elementParents = new HashMap<Character, Boolean>();
						for(int i = 0; i < parentNodes.size(); i++) 
                        {
							elementParents.put(parentNodes.get(i), A.get(parentNodes.get(i)));
						}
					}
					HashMap<Character, Boolean> node = new HashMap<Character, Boolean>();
					node.put(element.getKey(), element.getValue());

					result = result * compute(node, elementParents);
				}
			}
			else 
            {
				// sum over hidden values
				result = sumOverHidden(A);
			}
		}

		//System.out.println(result);
		return result;
	}

	public static double sumOverHidden(HashMap<Character, Boolean> A) 
    {
		List<HashMap<Character, Boolean>> currentCombos = new ArrayList<HashMap<Character, Boolean>>();
		List<HashMap<Character, Boolean>> hiddenCombos = new ArrayList<HashMap<Character, Boolean>>();
		int totalSize = bayesianNetwork.size();
		currentCombos.add(A);

		// get the hidden values
		List<Character> hiddenValues = new ArrayList<Character>();
		for(Map.Entry<Character, ArrayList<Character>> entry : bayesianNetwork.entrySet()) 
		{
            //System.out.println(entry);
			if(A.get(entry.getKey()) == null) 
            {
				hiddenValues.add(entry.getKey());
			}
		}

		// get both the T and F values of the hidden values 
		for(Character value : hiddenValues) 
        {
			int numberOfValues = currentCombos.size();
			HashMap<Character, Boolean> falseSet = null;
			HashMap<Character, Boolean> trueSet = null;
			for(int i = 0; i < numberOfValues; i++) 
            {
				trueSet = new HashMap<Character, Boolean>();
				trueSet.putAll(currentCombos.get(i));
				trueSet.put(value, true);
				falseSet = new HashMap<Character, Boolean>();
				falseSet.putAll(currentCombos.get(i));
				falseSet.put(value, false);

				currentCombos.add(trueSet);
				currentCombos.add(falseSet);

				// add when all variables are present
				if(trueSet.size() == totalSize) 
                {
					hiddenCombos.add(trueSet);
				}
				if(falseSet.size() == totalSize) 
                {
					hiddenCombos.add(falseSet);
				}
			}
		}				
		double result = 0;
		// sum over values
		for(int i = 0; i < hiddenCombos.size(); i++) 
        {
			result += compute(hiddenCombos.get(i), null);
		}

		return result;
	}

	public static double bnetProb(HashMap<Character, Boolean> A, HashMap<Character, Boolean> B) 
    {
		// get the value and the not value
		String value = "";
		String notValue = "";

		for(Map.Entry<Character, Boolean> entry : A.entrySet())  
        {
			// add letter (value)
			value += entry.getKey();
			notValue += entry.getKey();

			if(entry.getValue() == true) 
            {
				value += "t";
				notValue += "f";
			} 
            else 
            {
				value += "f";
				notValue += "t";
			}
		}
		if(B != null) 
        {
			value += "|";
			notValue += "|";

			for(Map.Entry<Character, Boolean> entry : B.entrySet()) 
            {
				value += entry.getKey();
				notValue += entry.getKey();

				if (entry.getValue() == true) 
                {
					value += "t";
					notValue += "t";

				} 
                else 
                {
					value += "f";
					notValue += "f";
				}
			}
		}

		double res = -1;

		if(networkProb.get(value) == null) 
        {
			if(networkProb.get(notValue) != null) 
            {
				res = 1 - networkProb.get(notValue);
			}
		} 
        else 
        {
			res = networkProb.get(value);
		}

		return res;
	}

    public static void createNetwork()
    {
        // add parent nodes
		bayesianNetwork.put('B', null);
		bayesianNetwork.put('E', null);
		ArrayList<Character>parents = new ArrayList<Character>();
		parents.add('B');
		parents.add('E');
		bayesianNetwork.put('A', parents);
		parents = new ArrayList<Character>();
		parents.add('A');
		bayesianNetwork.put('J', parents);
		parents = new ArrayList<Character>();
		parents.add('A');
		bayesianNetwork.put('M', parents);

		// add given probabilities to network
		networkProb.put("Bt", 0.001);
		networkProb.put("Et", 0.002);
		networkProb.put("At|BtEt", 0.95);
		networkProb.put("At|BtEf", 0.94);
		networkProb.put("At|BfEt", 0.29);
		networkProb.put("At|BfEf", 0.001);
		networkProb.put("Jt|At", 0.90);
		networkProb.put("Jt|Af", 0.05);
		networkProb.put("Mt|At", 0.70);
		networkProb.put("Mt|Af", 0.01);

        return;
    }
}