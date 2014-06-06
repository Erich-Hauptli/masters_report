$users = 10;		#Adjust number of users generated.
$job_chance = 4;	#Adjust rate of users obtaining jobs.
$b_chance = 1;		#Adjust rate of users obtaining Bachelor Degrees.
$m_chance = 5;		#Adjust rate of users obtaining Master Degrees.
$phd_chance = 7;	#Adjust rate of users obtaining PHDs.

@first_names = ();
@last_names = ();
@months = ("1", "2", "3","4","5","6","7","8","9","10","11","12");
@days_10s = ("0","1","2");  #ignoring 30th and 31st
@days_1s = ("1","2","3","4","5","6","7","8","9");  #ignoring 10th and 20th
@mail = ('@gmail.com', '@hotmail.com', '@yahoo.com');
@gender = ("M","F");

open(USERS, ">Users.csv") or die("Could not open first names file.");	#File to be generated.

open(FIRST_NAMES, "First_Names.csv") or die("Could not open first names file.");	#Open file containg list of first names.
foreach $line (<FIRST_NAMES>) {
 	chomp;
 	$line =~ s/^\s+|\s+$//g;	#Remove whitespace
 	push(@first_names, $line); 	#Add all names from file to first_names array.
 }
 close (FIRST_NAMES);
 

 
open(LAST_NAMES, "Last_Names.csv") or die("Could not open first names file.");	#Open file containing list of last names.
foreach $line (<LAST_NAMES>) {
 	chomp;
 	$line =~ s/^\s+|\s+$//g;	#Remove whitespace
 	push(@last_names, $line); 	#Add all names from file to last_names array.
 }
 close (LAST_NAMES);
 
open(YEARS, "Years.csv") or die("Could not open first names file.");	#Open file containing list of eligable birth years.
foreach $line (<YEARS>) {
 	chomp;
 	$line =~ s/^\s+|\s+$//g;	#Remove whitespace
 	push(@years, $line); 		#Add all years from file to years array.
 }
 close (YEARS);

##############  Create USERS #########################
 for($i=0;$i<$users;$i++){
 	$index   = rand @first_names;			#Pick a random element in the @first_names
 	$first_name = $first_names[$index];		#Pull the string of that random element and assign to $first_name
  	$index1   = rand @last_names;
 	$last_name = $last_names[$index1];
 	$index2 = rand @gender;
 	$sex = $gender[$index2];
 	$index3 = rand @months;
 	$month = $months[$index3];
 	$index4 = rand @days_10s;
 	$day_10 = $days_10s[$index4];
 	$index5 = rand @days_1s;
 	$day_1 = $days_1s[$index5];
 	$day = "$day_10$day_1";
 	$index6 = rand @years;
 	$year = $years[$index6];
 	$index7 = rand @mail;
 	$mail_route = $mail[$index7];
 	$email = "$first_name.$last_name$mail_route";
 	$num = "";
 	for($n=0;$n<10;$n++){					#Generate a random phone number
 		$index_num = rand @days_1s;
 		$num = "$num$days_1s[$index_num]";
 		if($n == 2 or $n == 5){
 			$num = "$num-";
 		}
 	}
 	############## Print out user information ################################
 	#profile, id, first name, last name, sex, birthday, e-mail,  phone number
 	print USERS "profile,$i,$first_name,$last_name,$sex,$month/$day/$year,$email,$num \n";
 	
 	###### Arrays below will be selected from at random ######################
 	@age_exit_hs = (16,17,17,18,18,18,18,18,19,19);			#Array containing age of high school graduation
 	@years_undergrad = (3,4,4,4,4,4,5,5,5,6);				#Array containing number of years spent in undergrad degree
 	@years_masters = (1,2);									#Array containing number of years spent in masters degree
 	@years_phd = (1,2,3,3,3);								#Array containing number of years spent in PHD degree
 	@years_job = (1,1,2,2,3,3,3,4,4,4,5,5,6,6,7,7,8,9,10);	#Array containing number of years spent in a particular job
 	@prob = (1,2,3,4,5,6,7,8,9,10);							#Array to generate basic probablity 
 	
 	$Current_Year = "2014";
 	$age = $Current_Year - $year;			#Determine user's age
 	$time_index = rand @age_exit_hs;		#Randomly select age of highschool graduation
 	$hs = $age_exit_hs[$time_index];
 	$time = $age - $hs;						#Calculate time in workforce/higher education
 	$jobs_held = 0;							#Set base counters
 	$UG = 0;
 	$MS = 0;
 	$PHD = 0;
 	 	
 	$start = $year + $hs;					#Set start to highschool graduation
 	while($time > 0){						#Decrement time for each job/education until at current year
 		
 		$prob_index = rand @prob;			#Roll probability dice
 		$probability = $prob[$prob_index];	
 		$chance = $jobs_held + $b_chance;	#Determine score needed to add to job/education history
 		if ($UG < 1 && $probability > $chance ){
 			$jobs_held++;
 			$UG = 1;
 			$start_ug = $start;
 			$end_ug_index = rand @years_undergrad;			#Select number of years to complete undergrad
 			$end_ug_year = $years_undergrad[$end_ug_index];
 			$end_ug = $start_ug + $end_ug_year;				#Calculate end year of degree
 			$start = $end_ug;
 			if($end_ug >= $Current_Year){					#If degree not completed by current year exit loop
 				$time = 0;
 				$end_ug = "Current";
 			}else{
 				$time = $time - $end_ug_year;
 			}
 			#Print out education instance
 			#education, id, degree, specialization, school, start_date, end_date
 			print USERS "education,$i,Bacholers,specialization,school,$start_ug,$end_ug \n";
 		}
 	
 		$prob_index = rand @prob;			#Roll probability dice
 		$probability = $prob[$prob_index];
 		$chance = $jobs_held + $m_chance;	#Determine score needed to add to job/education history
 		if($MS < 1 && $UG == 1 && $probability > $chance){	#Undergrad degree required
 			$jobs_held++;
 			$MS = 1;
 			$start_ms = $start;
 			$end_ms_index = rand @years_masters;			#Select number of years to complete masters degree		
 			$end_ms_year = $years_masters[$end_ms_index];	
 			$end_ms = $start_ms + $end_ms_year;				#Calculate end year of degree
 			$start = $end_ms;
 			if($end_ms >= $Current_Year){					#If degree not completed by current year exit loop
 				$time = 0;
 				$end_ms = "Current";
 			}else{
 				$time = $time - $end_ms_year;
 			}
 			#Print out education instance
 			#education, id, degree, specialization, school, start_date, end_date
 			print USERS "education,$i,Masters,specialization,school,$start_ms,$end_ms \n";
 		}
 	
 		$prob_index = rand @prob;			#Roll probability dice
 		$probability = $prob[$prob_index];
 		$chance = $jobs_held + 7;			#Determine score needed to add to job/education history
 		if($PHD < 1 && $MS == 1 && $probability > $chance){		#Masters degree required
 			$jobs_held++;
 			$PHD = 1;
 			$start_phd = $start;
 			$end_phd_index = rand @years_phd;					#Select number of years to complete PHD
 			$end_phd_year = $years_phd[$end_phd_index];
 			$end_phd = $start_phd + $end_phd_year;
 			$start = $end_phd;
 			if($end_phd >= $Current_Year){						#If degree not completed by current year exit loop
 				$time = 0;
 				$end_ms = "Current";
 			}else{
 				$time = $time - $end_phd_year;
 			}
 			#Print out education instance
 			#education, id, degree, specialization, school, start_date, end_date
 			print USERS "education,$i,PHD,specialization,school,$start_phd,$end_phd \n";
 		}
 		
 		$prob_index = rand @prob;			#Roll probability dice
 		$probability = $prob[$prob_index];
 		$chance = $jobs_held + $job_chance;	#Determine score needed to add to job/education history
 		if($probability <= $chance){
 			$start_job = $start;
 			$end_job_index = rand @years_job;		#Select number of years in job
 			$end_job_year = $years_job[$end_job_index];
 			$end_job = $start_job + $end_job_year;
 			$start = $end_job;
 			if($end_job >= $Current_Year){			#If job not completed by current year exit loop
 				$time = 0;
 				$end_job = "Current";
 			}else{
 				$time = $time - $end_job_year;
 			}
 			$jobs_held++;
 			#Print out job instance
 			#job, id, title, company, location, start_date, end_date
 			print USERS "job,$i,title,company,location,$start_job,$end_job \n";
 		}
 	}  	
 }