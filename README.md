Erich Hauptli's Masters Report
==============================
There are 3 different trees in this repository.


GENERATE DATA

                  Contains the code to generate mock data to be loaded into a database.  This will produce a .csv file which can be 
                  consumed by the Connections tree.
                  

    GenUserData.pl  --- This will generate the .csv file that can be loaded into a database.  At the top of this file the number of users and the frequency a node is entered can be controled through variables. 
    *.txt  && *.csv --- These files contain mock data that is used to create the Users.csv file that is loaded into the database.
    Users.csv       --- This is the file that is generated by GenUserData.pl to be loaded into the database.  This file needs to be coppied over into the Connections tree.
    
    
CONNECTIONS

                  This tree provides a means to interact with the database, process the data, and draw conclusions about the data.

    connections
        This package contains the code that generates the arff file, finds the edges between job and education nodes, produces an ordering for the nodes, and catagoizes data about each node.
        
        Connections_Test --- Run this to print out all results based on the fields (common_field & common_field_value) toward the top of this file.  This will run against the .db files.  Note: If the .db files do not exist, they can be created by loading User.csv into User_Test
        
    data_collection
        This package contains the code that pulls in all the users from a database that match a search field, it can then pull in all the data about these users, and all the data available about a specific node.
        
    files
        This package simply allows for reading and writting to a file.
        
    insights
        This package uses Weka to create clusters of data based on an arff file.  Eventually it will also draw conclusions based on those clusters.
        
        Insights_Test --- Run this to read in an arff file (currently defined to education.arff), run clustering against the data and then print out the results.
        
    json_interface
        This package passes in a json object, calls the connections code, and returns all the results in a large json package.
        
        JSON_TEST --- Run this to return JSON results based on the fields (common_field & common_field_value) toward the top of this file.  This will run against the .db files.  Note: If the .db files do not exist, they can be created by loading User.csv into User_Test
        
    print
        This package prints out the results of connections in a readable fashion.
        
    sql
        This package provides interfaces to create and work with a MySQL database.
        
    tools
        This package provide methods to do fuzzy matching and compare arraylists.
        
    user
        This package provides an interface for the SQL database and the data collection.
        
        User_Test --- Run this to load in a Users.csv file and create the database.  Note: If the databases already exist and you are adding in additional data, comment out the setup line.  Also note, this runs off of the Users.csv file in the Connections tree, not the GenerateData tree.
        
REPORT

                  This tree is the Latex masters report and is just starting out.  It is based on Will O'Donnell's master report.
