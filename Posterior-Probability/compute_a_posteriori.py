# Noah Wood
# 1001705291

import sys

# get command line arguments
numArgs = len(sys.argv)

# check args
if numArgs > 2:
    print("incorrect arguments, need: python3 compute_a_posteriori.py <observations>")
    exit(0)

# inital probabilities for each bag
# will change based on observations
h1 = .1
h2 = .2
h3 = .4
h4 = .2
h5 = .1

# probability of cherry and lime
pc1 = 1
pc2 = .75
pc3 = .5
pc4 = .25
pc5 = 0

pl1 = 0
pl2 = .25
pl3 = .5
pl4 = .75
pl5 = 1

# get the probaility that next is cherry and for lime
pNextC = h1 * pc1 + h2 * pc2 + h3 * pc3 + h4 * pc4 + h5 * pc5

pNextL = h1 * pl1 + h2 * pl2 + h3 * pl3 + h4 * pl4 + h5 * pl5

try:
    file = open("result.txt", "w")
    if len(sys.argv) > 1:
        file.write("Observation Sequence Q: " + sys.argv[1])
        file.write("Length of Q: \n")
    else:
        file.write("Observation Sequence Q: none\n")
        file.write("Length of Q: 0\n\n")
        file.write("P(h1 | Q) = " + str(h1) + "\n")
        file.write("P(h2 | Q) = " + str(h2) + "\n")
        file.write("P(h3 | Q) = " + str(h3) + "\n")
        file.write("P(h4 | Q) = " + str(h4) + "\n")
        file.write("P(h5 | Q) = " + str(h5) + "\n")
        file.write("\nProbaility that the next candy we pick will be C, given Q: " + str(pNextC))
        file.write("\nProbaility that the next candy we pick will be L, given Q: " + str(pNextL))


except:
    print("File error.")
