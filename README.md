# **Launch Interceptor Program: Decide**
### Assigment 1 of DD2480
*The repository at hand is an implementation of the **Launch Interceptor Program** 
according to the requirements specifications as given in assignment 1.*

As part of a hypothetical anti-ballistic missile system, the Decide function is used to determine 
whether to launch interceptor missiles given some radar tracking information.

The Decide function comprises four main parts:
1. The Conditions Met Vector (CMV)
2. The Logical Connector Matrix (LCM)
3. The Preliminary Unlocking Matrix (PUM)
4. The Preliminary Unlocking Vector (PUV)
5. The Final Unlocking Vector (FUV)

The CMV consists of 15 Launch Interceptor Conditions (LIC), each of which perform calculations based
on the input data and returns a boolean value of whether the condition is met based on the existing data.
The results of these 15 LICs is stored in the CMV, which is then used in conjunction with the LCM
in order to determine the PUM. The LCM is a 15x15 matrix that state which of the LICs have to be considered
together when evaluating a given missile threat. The PUV states the single LICs that have to be considered.
Based on the PUV and the generated PUM, the FUV can be derived,
and if all entries of the FUV is true, the program approves of launching the anti-ballistic missiles.

### Building and running
The program is built using [**Maven**](https://maven.apache.org), a build tool
that downloads the necessary dependencies to run the project. It can also run the test suite to verify that
the functionality of the program is intact.

Dependencies:
* Junit 4.12

#### Running with Maven via CLI:
>mvn -B verify --file pom.xml

#### Running with IntelliJ:
1. Open the project in IntelliJ.
2. Select "Load project with Maven" when prompted by IntelliJ. 
3. Run the complete test suite, the complete Decide class, or individual functions as you see fit.


### Continuous Integration
Continuous Integration is set up using GitHub Actions and the provided Java with Maven Template.
On each push or pull request to the main branch, the entire test suite will run in order to detect any errors as they arise.

## Documentation
[Link to website hosting JavaDocs?](https://docs.google.com)  
Where do we want to host the Javadocs?


## Essence
Answers the following questions in one paragraph, according to the Essence checklist:  
In what state are you in? Why? What are obstacles to reach the next state?

## Statement of Contributions
| Emil        | Simon    | Viktor | Joakim | Jovan        |
|-------------|----------|--------|--------|--------------|
| Insert text | in these | cells  | Setting up the environment (File structure, maven file, etc) | presentation |
|             |          |        | LIC #0, #5, #8       |              |
|             |          |        | Implemented end-to-end tests       |              |
|             |          |        | Final implementation of decide function       |              |
|             |          |        | Refactoring, reviewing PRs, bug fixing       |              |
