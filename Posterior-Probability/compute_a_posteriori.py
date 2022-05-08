# Noah Wood
# 1001705291

import sys

# get number command line arguments
numArgs = len(sys.argv)

# check args
if numArgs > 2:
    print("incorrect arguments, format: python compute_a_posteriori.py <observations>")
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
        file.write("\nLength of Q: " + str(len(sys.argv[1])))
        counter = 1

        for char in sys.argv[1]:
            # evaluate based on the observation
            # other characters than 'C' or 'L' will be ignored.
            if char == 'C':
                h1 = pc1 * h1 / pNextC
                h2 = pc2 * h2 / pNextC
                h3 = pc3 * h3 / pNextC
                h4 = pc4 * h4 / pNextC
                h5 = pc5 * h5 / pNextC

                # get the probaility that next is cherry and for lime
                pNextC = h1 * pc1 + h2 * pc2 + h3 * pc3 + h4 * pc4 + h5 * pc5
                pNextL = h1 * pl1 + h2 * pl2 + h3 * pl3 + h4 * pl4 + h5 * pl5
                
            elif char == 'L':
                h1 = pl1 * h1 / pNextL
                h2 = pl2 * h2 / pNextL
                h3 = pl3 * h3 / pNextL
                h4 = pl4 * h4 / pNextL
                h5 = pl5 * h5 / pNextL

                # get the probaility that next is cherry and for lime
                pNextC = h1 * pc1 + h2 * pc2 + h3 * pc3 + h4 * pc4 + h5 * pc5
                pNextL = h1 * pl1 + h2 * pl2 + h3 * pl3 + h4 * pl4 + h5 * pl5

            # display
            file.write("\n\nAfter Observation " + str(counter) + " = " + char + "\n")
            counter += 1
            file.write("P(h1 | Q) = " + str(round(h1,5)) + "\n")
            file.write("P(h2 | Q) = " + str(round(h2,5)) + "\n")
            file.write("P(h3 | Q) = " + str(round(h3,5)) + "\n")
            file.write("P(h4 | Q) = " + str(round(h4,5)) + "\n")
            file.write("P(h5 | Q) = " + str(round(h5,5)) + "\n")
            file.write("\nProbaility that the next candy we pick will be C, given Q: " + str(round(pNextC,5)))
            file.write("\nProbaility that the next candy we pick will be L, given Q: " + str(round(pNextL,5)))
    else:
        # since no observations, display current probabilities

        file.write("Observation Sequence Q: none\n")
        file.write("Length of Q: 0\n\n")
        file.write("P(h1 | Q) = " + str(round(h1,5)) + "\n")
        file.write("P(h2 | Q) = " + str(round(h2,5)) + "\n")
        file.write("P(h3 | Q) = " + str(round(h3,5)) + "\n")
        file.write("P(h4 | Q) = " + str(round(h4,5)) + "\n")
        file.write("P(h5 | Q) = " + str(round(h5,5)) + "\n")
        file.write("\nProbaility that the next candy we pick will be C, given Q: " + str(round(pNextC,5)))
        file.write("\nProbaility that the next candy we pick will be L, given Q: " + str(round(pNextL,5)))

except:
    print("File error.")
