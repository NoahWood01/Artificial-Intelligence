# Bayesian Networks #
This program takes in input of variables to determine probability based on values stored in a bayesian network.

Five variables : B, E, A, J, M where B,E are parents of A, and A is parent of J and parent of M. With a t or f indicating true or false respectively.


### Compile with ###

`javac bnet.java`
`java bnet <1 - 6 arguments>`

where arguments are a string of two characters such as `Mf Et` or combined with a given `Mt given Ef`

### Ex ###

`javac bnet.java`
`java bnet Jf Mt given Et`

or

`javac bnet.java`
`java bnet Jf Mt`