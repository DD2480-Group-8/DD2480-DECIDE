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
All Java code in the repository is complemented with JavaDocs, which can easily be generated within an IDE such as IntelliJ or Eclipse.

## Essence
There is no obvious state in which we find ourselves in the essence way-of-working checklist, but rather that we recognize that our group span multiple states. The first state, “Principles Established”, is clearly passed, due to the “Meet your group”-assignment, where all of the points were discussed, however in the following states, some points are fulfilled where others are not. For example, in “Foundations Established”, we have selected key practices and tools, enough to let the work start, but perhaps the gaps that exists between practices needed and practices available haven’t been explicitly analyzed and understood. At the same time, multiple points from “In Use” and some from “In Place”, such as “Practices being used to do real work”, “Practices are being adapted to the team’s way of working”, and “Practices and tools are being used by the whole team to perform their work”, are fulfilled. This leads us to believe that we are mostly in the “In Use”-state, but are also transitioning out of the “Foundation Established”-state and in to the “In Place”-state. The obstacles of reaching further is actually analyzing and thinking about how the work proceeds, to continue with open communication and feedback, and to question if the practices and tools in place are working, and whether we can adopt new ones that improve the work experience within the group.

## Statement of Contributions
| Emil        | Simon    | Viktor | Joakim | Jovan        |
|-------------|----------|--------|--------|--------------|
| Insert text | in these | cells  | before | presentation |
|             |          |        |        |              |
|             |          |        |        |              |
|             |          |        |        |              |
