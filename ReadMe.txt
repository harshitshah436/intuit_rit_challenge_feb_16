Intuit-Rit-Challange_Feb 2016
Problem: Your customer wants to go on a road trip next month and needs to save some money in the next 3 months in order to pay for it.
URL: https://github.com/intuit-recruiting/rit-challenge

Author: Harshit Shah

The archive file contains following directory structure. It is an eclipse project directory.

Files Information:
    Intuit-Rit-Challange/bin                            - Compiled class files
    Intuit-Rit-Challange/lib                            - Library used in project (Don't forget to add jar while compiling)
    Intuit-Rit-Challange/src/                           - All source files. (Main.java, Transaction.java, StringMatching.java, ReservoirSampling.java)
    Intuit-Rit-Challange/dictionary.txt                 - Add store/transaction details if found new. Currently added from the given sample data which could probably same because of same locality for any customer.
    Intuit-Rit-Challange/Intuit_Rit_Feb_16.bat          - Batch file to directly run program from generated executable jar in /lib directory.
    Intuit-Rit-Challange/ReadMe.txt                     - Read me file for general information and program running information.
    Intuit-Rit-Challange/transactions-person-A.csv      - given input sample CSV file 1.
    Intuit-Rit-Challange/transactions-person-B.csv      - given input sample CSV file 2.
    

Developer's manual:

    Method 1: Using Eclipse tool
    1) Copy unzipped directory 'Intuit-Rit-Challange' into current workspace.
    2) File->Import, then select "Existing project into workspace". Click next.
    3) Select directory and finish the import.
    
    Method 2: From command line
    1) Include 'commons-csv-1.1.jar' from lib folder to current Java classpath.
    2) Then compile the program as below.
        javac Main.java - compiles and loads all the java sources.

    Note: If you want to change POOL size and Reservoir size, change them in 'Main.java' file. Pool size is number of transactions to be selected from total available transactions with higher spent value. Reservoir size to create a reservoir by applying reservoir sampling algorithm. It will creates a number of recommendation based on reservoir size. As of now reservoir size is 5 and POOL size is 12, so algorithm recommends 5 transactions from the pool of 12 transactions for highly spent money.
    
User's manual:
    To run the program after setting up project and compiled files follow below methods.
    
    Method 1: Execute a batch file (Simplest)
    1) execute 'Intuit_Rit_Feb_16.bat' batch file. If you want to change the dataset just edit batch file and change the file name as explained in it.
    
    Method 2: Using Eclipse tool
    1) Importing project as explained above in Developer's manual, just run the Main.java file.
    
    Method 3: From command line
    1) Put input file and 'dictionary.txt' file into generated class directories. In our case 'bin'.
    2) Now run the program as below.
        java Main <input_file> - run this command.

        Here,   <input_file> - file must be in csv format

        
Implemented Algorithm:
    1) Load input_file and 'dictionary.txt' files into lists.
    2) Create a Map<String, Double> from input list which map a dictionary word (related to transaction) to the spent amount. Here, string matching algorithm is applied.
    3) Sort the created transaction map in descending order of it's values (spent amount). Highest amount transaction would be at lower indexes.
    4) Now create a list of size (poolsize,n) from this transaction map.
    5) Apply reservoir sampling to this list of size n and select randomly k samples and insert it into reservoir list of size k.
    6) Display this transactions after calculating month average spent on them and how much money can be saved in next 3 months.
    

Testing:
    1) I run this program on windows 10 machine by executing batch file (Intuit_Rit_Feb_16.bat) with input_file 'transactions-person-A.csv'.
    
    2) Generated Output:
    
    File: transactions-person-A.csv - total records read: 673

    You should stop spending following average amount per month.

                             Description         Amount
                             -----------         ------
                               EXXON GAS         8.89
                                  PAYPAL         23.28
                         TARGET PURCHASE         21.97
                                  AMAZON         17.12
                               NORDSTROM         11.10

    So, you can save upto total 247 bucks in next 3 months.


    Note: 'Rent' and 'Bank withdraw' transactions are ignored considering they are spent behind your basic needs.
            Resident payment for Rent (per month): $888.19
            Bank of America ATM Withdrawal (per month): $33.89

    3) Keep in mind that output will change every time because we are applying randomized algorithm, so but will be only from 12 highly spent transactions. 'Rent' and 'Bank withdrawal' transactions are ignored.
    

 Future Work:
    - I have concentrated on algorithms, so could focus more on visual recommendations.
    - For future work, recommendations can be shown in form of graphs, pie charge by domains of transactions.
    - For example input transactions may classified into Retail, Supermarkets, Restaurants, etc... to graphically represent it.