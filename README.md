# Performance Analisys of Developers - Final Year Project UOM 2017

This module identifies the expertise areas of a developer based on a pre-defined expertise dictionary and clasify/rank developer on each expertise area.

# What is an Expertise Area?

An expertise area is the atomic level skills that a developer should have in writing code. These skills defer from one programming language to another therefore domain specific. In identifying developer expertise first we extract the available expertise areas to a common dictionary which includes all the available atomic skills domain wise and having the ability to expand. We introduce a set of rules in identifying a skill. These rules are domain specific and relies on the assumption that every developer follows the domain specific standard naming conventions in coding. 

# Expertise Dictionary

The expertise dictionary is generated continuously processing the projects given as input to the system. This lets the dictionary to grow automatically avoiding manual insertion of expertise areas. A set of rules define these expertise areas. Considering Java as the domain and the standard naming conventions used in Java, the set of rules defined will be as follows. 

•	Class - C, Interface - I, Method - m, Variable - v, Constant - CONST
•	Common syntax 
  o	Object Modifiers(OM) - final, static
  o	Access Modifiers(AM) - public, protected, private
  o	Data Types(DT) - int, String, float, double etc
  o	Return Type(RT) - void, Data Types
  o	Other(O) - System, out, print, println, new, import,return etc

# Quantifying expertise of developer 

Quantifying is done in two methods.
•	Frequency of updates done to code base
  This is the basic measurement used in identifying the areas a developer has engaged in and the level of contribution. Social Coding Platforms allow us to retrieve the changes done to a certain file by a certain developer. These code fragments can be used in identifying the number of additions or changes done by a developer. This assessment is done based on the concept that level of expertise increases with experience. 
•	Usage Expertise
  Measuring usage expertise is done method wise. This is based on the concept that if a developer uses a method, he has an idea of what it does even though he’s not the one who implemented
 
