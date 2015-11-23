# OOFileSystem
A Simple File System Designed With Object Oriented Approach

This project is a side project. It has three parts: Node and two classes inherited from it: File and Directory, which constitute
the skeleton of the file system; FileSystem part, which facilitates access and modification of the file system; FileSystemInteractive part,
which facilitaes user interaction with the file system from command line.

There are some design decisions I think I need to clarify here.

First, it's the use of java reflection in FileSystemInteractive. I am aware of the downsides of java reflection and have explored
other possibilities, the most seemingly promising one was command pattern. However, after a certain while of exploring such 
possibilities, I found no way to solve the problem from either directly adopting the approaches or tweeking them a bit. I ended up using 
java reflection.

Second, I used Node as an abstract super class for File and Directory in order to provide a rather uniform interface to the client 
program. In the Node class, I have written many methods that only throws exceptions. This is to make the program compile, because
compiler cannot compile if a method only exists in the sub class but not super class. Alternatively, I can put an list of Directory
and an list of File inside Directory, and let the user program check if one "entry" is a Directory or a File.However, I don't see any 
advantage compared to the previous approach. So I stick to making the Node class.
