# U.S Congressional-Redistricting

<b>Background info : </b><br>
A Gerrymander is a voting district that is designed to serve some political purpose. The name refers to both a salamander and Eldridge Gerry, whose newly created voting district about 200 years ago was said to resemble a salamander. Within the past 10 years, databases for voter characterization as well as tools for precise map generation have made it possible to create congressional districts that favor the party responsible for the creation of the districts. Redistricting is done in states where census data requires a change in the number of delegates in the state, and the 2010 census triggered redistricting in a number of states. Many of these redistricting efforts resulted in a shift in the political representation in the states. As the realization of the impact of these changes has grown, various technical approaches to the issue have been proposed, some as quantitative measures of the presence of Gerrymandering, others as legal challenges to redistricting, and yet others as draft bills in Congress to minimize the effect of future redistricting. Many of the redistricting changes following the 2010 census used provisions of the Voting Rights Act (VRA) in way not intended by the people responsible for the VRA. For example, the VRA provided for majority-minority districts, which were intended as a means to ensure representation of minority groups in Congress. However, the VRA was used to "pack" districts, which promoted Gerrymandered districts, and also fewer majority-minority district than might otherwise have been possible.

<b>Project purpose : </b><br>
The system will allow for the generation of congressional district boundaries without political influence but with the maximum number of majority-minority districts. System gatheres data associated with this topic, analyzes the data using many of the proposed measures of Gerrymandering, presentes the data in a way that highlighted the effects of gerrymandering, and explores various algorithms for automatic redistricting.

<b>Project components : </b><br>
<ul>
<b>GUI</b> - The system will include a Web user interface in which users interact with the system. The GUI will allow users to select a state to analyze and display actual congressional districts in a map of the selected state, while presenting data associated with congressional districting (e.g., measures of the effect of gerrymandering).<br><br>

<b>Database</b> - Data used by the system is contained in a 3-state database. This data includes congressional election results for the two most recent elections (2016 and 2018) and presidential election results for 2016. The system also includes geospatial data describing the boundary of each state, congressional district, interim district, and voting precinct.<br>

<b>Majority-Minority Suitability (Phase 0)</b> - System includes a feature for the user to determine if a state complies with the Supreme Court requirements related to the Voting Rights Act. For Majority-Minority districts to be created, the court required that 1) the district be sufficiently large and compact, 2) the minority group is politically cohesive, and 3) the white majority votes as a bloc.<br>
  
<b>Congressional District Generation</b> - System will include a 2-step algorithmic approach to automate district generation. The first step uses a graph partition algorithm that will generate an initial set of congressional districts, the number of which is specified by the user in the GUI. The second step refines the initial set of districts using simulated annealing. In both phases of the process, the solution goal will be to generate the maximum number of majority-minority congressional districts, while adhering to constraints and objectives specified by the user (e.g., district compactness).<br>
  
<b>Preprocessing</b> - This sub-system will break out precinct boundary data if the data source groups it together, identify or correct data problems, determine precinct neighbors, combine multiple data sources (e.g., census) to generate complete precinct data.<br>
  </ul>
  
  <b>Languages, Frameworks, Libraries etc. : </b><br>
  <ul>
  <b>Front-End : </b>JavaScript, HTML, CSS. Standard stack for front-end.<br>
  <b>Back-End : </b>Java. OOP language, fits well with the complex design of the system.<br>
  <b>Database : </b>MySQL, standard CRUD operations.<br>
  <b>Preprocessing : </b> Python. Great for data science related issues, in this case the sub-system has to retrieve raw data from multiple data source, analyze, clean, and put it together to feed it to DB.<br>
  <b><a href="https://leafletjs.com/" target="_blank">Leaflet</a> : </b>Leaflet is a widely used open source JavaScript library used to build web mapping applications. Another alternative can be Google Maps<br>
  <b><a href="https://hibernate.org/" target="_blank">Hibernate</a> : </b>Hibernate ORM is an object-relational mapping tool for the Java programming language. It provides a framework for mapping an object-oriented domain model to a relational database.<br>
  <b><a href="https://pypi.org/project/Fiona/" target="_blank">Fiona</a> : </b>Python library for reading and writing spatial data files. Used for preprocessing component.
  </ul
  
  <b>Demo pictures : </b><br>
  <p>(Kentucky precincts zoomed in)</p>
  <img src="https://github.com/tormanburak/U.S-Congressional-Redistricting/blob/master/demopics/ss1.PNG"></img>
  <p>(Texas district boundaries)</p>
  <img src="https://github.com/tormanburak/U.S-Congressional-Redistricting/blob/master/demopics/ss2.PNG"></img>
  <b>(Texas precinct boundaries)</b>
  <img src="https://github.com/tormanburak/U.S-Congressional-Redistricting/blob/master/demopics/ss3.PNG"></img>
  <p>(Texas precinct boundaries zoomed in)</p>
  <img src="https://github.com/tormanburak/U.S-Congressional-Redistricting/blob/master/demopics/ss4.PNG"></img>
