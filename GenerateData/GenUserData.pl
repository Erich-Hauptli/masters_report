$users = 5000;			#Adjust number of users generated.
$job_chance = 4;		#Adjust rate of users obtaining jobs.
$b_chance = 2;			#Adjust rate of users obtaining Bachelor Degrees.
$m_chance = 5;			#Adjust rate of users obtaining Master Degrees.
$phd_chance = 7;		#Adjust rate of users obtaining PHDs.
$change_companies = 7;	#Adjust rate of users changing companies in a job change.
$move = 9;				#Frequency users move for a job

@months = ("1", "2", "3","4","5","6","7","8","9","10","11","12");
@days_10s = ("0","1","2");  #ignoring 30th and 31st
@days_1s = ("1","2","3","4","5","6","7","8","9");  #ignoring 10th and 20th
@mail = ('@gmail.com', '@hotmail.com', '@yahoo.com');
@gender = ("M","F");

open(USERS, ">Users.csv") or die("Could not open first names file.");	#File to be generated.

open(FIRST_NAMES, "First_Names.csv") or die("Could not open first names file.");	#Open file containg list of first names.
@first_names = ();
foreach $line (<FIRST_NAMES>) {
 	chomp $line;
 	$line =~ s/^\s+|\s+$//g;	#Remove whitespace
 	push(@first_names, $line); 	#Add all names from file to first_names array.
 }
 close (FIRST_NAMES);
 

 
open(LAST_NAMES, "Last_Names.csv") or die("Could not open last names file.");	#Open file containing list of last names.
@last_names = ();
foreach $line (<LAST_NAMES>) {
 	chomp $line;
 	$line =~ s/^\s+|\s+$//g;	#Remove whitespace
 	push(@last_names, $line); 	#Add all names from file to last_names array.
 }
 close (LAST_NAMES);
 
open(YEARS, "Years.csv") or die("Could not open years file.");	#Open file containing list of eligable birth years.
@years = ();
foreach $line (<YEARS>) {
 	chomp $line;
 	$line =~ s/^\s+|\s+$//g;	#Remove whitespace
 	push(@years, $line); 		#Add all years from file to years array.
 }
 close (YEARS);
 
open(COLLEGES, "Colleges.txt") or die("Could not open colleges file.");	#Open file containing list of last names.
@colleges = ();
foreach $line (<COLLEGES>) {
	chomp $line;
	$line =~ s/\r\n?/\n/g;
 	push(@colleges, $line); 	#Add all names from file to last_names array.
}
close (LAST_NAMES);

open(LOCATIONS, "Locations.csv") or die("Could not open locations file.");	#Open file containing list of last names.
@locations = ();
foreach $line (<LOCATIONS>) {
	chomp $line;
	$line =~ s/\r\n?/\n/g;
 	push(@locations, $line); 	#Add all names from file to last_names array.
}
close (LOCATIONS);

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
 	#profile, id, last name, first name, birthday, sex, e-mail,  phone number
 	print USERS "profile,$i,$last_name,$first_name,$month/$day/$year,$sex,$email,$num \n";
 	
 	

 	
 	
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
 	$specialization_ug = "ND";			#Initialize people to no degree
 	$m_specialization = "";
 	$phd_specialization = "";
 	$company = "NJ";					#Initialize people to no job
 	$location = "NL";					#Initialize job to no location
 	$node = "";
 	 	
 	$start = $year + $hs;					#Set start to highschool graduation
 	while($time > 0){						#Decrement time for each job/education until at current year
 		
 		############  BACHELORS DEGREE ######################################################################
 		@GPA = (2,3.0,3.1,3.2,3.3,3.4,3.5,3.6,3.7,3.8,3.9,4.0);
 		$prob_gpa = rand @GPA;
 		$final_gpa = $GPA[$prob_gpa];
 		$prob_index = rand @prob;			#Roll probability dice
 		$probability = 10;#Force into bachelors#$prob[$prob_index];	
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
 			
 			$college_index = rand @colleges;
 			$college = $colleges[$college_index];
 			@univ = ();
 			@univ = split(':',$college);
 			$school_ug = $univ[0];
 			@spec = ();
 			@spec = split(',',$univ[1]);
 			$spec_index = rand @spec;
 			$specialization_ug = $spec[$spec_index];
 			$node = $specialization_ug;
 			#Print out education instance
 			#education, id, degree, school, start_date, end_date, specialization 
 			print USERS "education,$i,Bacholers,$school_ug,$start_ug,$end_ug,$specialization_ug,$final_gpa\n";
 		}
 	
 		############  MASTERS DEGREE ######################################################################
 		@MGPA = (3.0,3.3,3.5,3.7,3.8,3.9,4.0);
 		$prob_mgpa = rand @MGPA;
 		$final_mgpa = $MGPA[$prob_mgpa];
 		$prob_index = rand @prob;			#Roll probability dice
 		$probability = $prob[$prob_index];
 		$chance = $jobs_held + $m_chance;	#Determine score needed to add to job/education history
 		if($MS < 1 && $UG == 1 && $probability > $chance){	#Undergrad degree required
 			$jobs_held++;
 			$MS = 1;
 			$start_ms = $start;
 			$end_ms_index = rand @years_;			#Select number of years to complete masters degree		
 			$end_ms_year = $years_masters[$end_ms_index];	
 			$end_ms = $start_ms + $end_ms_year;				#Calculate end year of degree
 			$start = $end_ms;
 			if($end_ms >= $Current_Year){					#If degree not completed by current year exit loop
 				$time = 0;
 				$end_ms = "Current";
 			}else{
 				$time = $time - $end_ms_year;
 			}
 			
 			open(COLLEGES, "Colleges.txt") or die("Could not open colleges file.");	#Open file containing list of last names.
 			@advanced_schools = ();
			foreach $line (<COLLEGES>) {
				chomp $line;
				$line =~ s/\r\n?/\n/g;
				if ($line =~ /$specialization_ug/){
					@univ_m = ();
					@univ_m = split(':',$line);
 					$school_m = $univ_m[0];
 					push(@advanced_schools, $school_m); 	#Add all schools for undergrad specialty.
				}	
			}
			close (COLLEGES);

			$m_school_index = rand @advanced_schools;			#Select number of years to complete masters degree		
 			$master_school = $advanced_schools[$m_school_index];	
 			
 			open(MASTERS, "Masters.txt") or die("Could not open Masters file.");	#Open file containing list of last names.
 			@m_special = ();
 			#print "Bachelors: $specialization_ug\n";
			foreach $line (<MASTERS>) {
				chomp $line;
				$line =~ s/\r\n?/\n/g;
				if ($line =~ /$specialization_ug/){
					#print "$line\n";
					@spec_m = ();
					@spec_m = split(':',$line);
 					@special_m = split(',',$spec_m[1]);	
				}	
			}
			close (MASTERS);
 			
			$m_special_index = rand @special_m;			
 			$m_specialization = $special_m[$m_special_index];
 			$node = $m_specialization;
 			
 			#Print out education instance
 			#education, id, degree, school, start_date, end_date, specialization 
 			print USERS "education,$i,Masters,$master_school,$start_ms,$end_ms,$m_specialization,$final_mgpa\n";
 		}
 	
 		###############  PHD ######################################################################
 		@PGPA = (3.3,3.5,3.8,4.0);
 		$prob_pgpa = rand @PGPA;
 		$final_pgpa = $PGPA[$prob_pgpa];
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

 			open(COLLEGES, "Colleges.txt") or die("Could not open colleges file.");	#Open file containing list of last names.
 			@advanced_schools = ();
			foreach $line (<COLLEGES>) {
				chomp $line;
				$line =~ s/\r\n?/\n/g;
				if ($line =~ /$specialization_ug/){
					@univ_m = ();
					@univ_m = split(':',$line);
 					$school_m = $univ_m[0];
 					push(@advanced_schools, $school_m); 	#Add all schools for undergrad specialty.
				}	
			}
			close (COLLEGES);
 			
			$phd_school_index = rand @advanced_schools;			#Select number of years to complete masters degree		
 			$phd_school = $advanced_schools[$phd_school_index];
 			
 			open(PHD, "PHD.txt") or die("Could not open PHD file.");	#Open file containing list of last names.
 			@phd_special = ();
 			#print "Masters: $m_specialization\n";
			foreach $line (<PHD>) {
				chomp $line;
				$line =~ s/\r\n?/\n/g;
				if ($line =~ /$m_specialization/){
					#print "$line\n";
					@spec_phd = ();
					@spec_phd = split(':',$line);
 					@phd_special = split(',',$spec_phd[1]);	 	
				}	
			}
			close (PHD);
 			
			$phd_special_index = rand @phd_special;			
 			$phd_specialization = $phd_special[$phd_special_index];
 			$node = $phd_specialization;
 			#print "PHD: $phd_specialization\n";
 			
 			#Print out education instance
 			#education, id, degree, school, start_date, end_date, specialization 
 			print USERS "education,$i,PHD,$phd_school,$start_phd,$end_phd,$phd_specialization,$final_pgpa\n";
 		}
 		
 		################  JOBS ######################################################################
 		$break = 0;
 		$prob_index = rand @prob;			#Roll probability dice
 		$probability = $prob[$prob_index];
 		$chance = $jobs_held + $job_chance;	#Determine score needed to add to job/education history
 		if($probability <= $chance && $break == 0){
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
 			
 			$prob_index = rand @prob;			#Roll probability dice
 			$probability = $prob[$prob_index];
 			if ($company eq "NJ"){
 				$probability = 11;	#If no previous company exists, probabilty is guarenteed to generate new company.
 			}
 			if($probability > $change_companies){
 				open(COMPANIES, "Companies.txt") or die("Could not open companies file.");	#Open file containing list of last names.
 				@companies = ();
				foreach $line (<COMPANIES>) {
					chomp $line;
					$line =~ s/\r\n?/\n/g;
					if ($line =~ /$specialization_ug/){
						@comp = ();
						@comp = split(':',$line);
 						$company_tmp = $comp[0];
 						push(@companies, $company_tmp); 	#Add all schools for undergrad specialty.
					}	
				}
				close (COLLEGES);
				$company_index = rand @companies;			#Select number of years to complete masters degree		
 				$company = $companies[$company_index];
 				
 				$prob_index = rand @prob;			#Roll probability dice
 				$probability = $prob[$prob_index];
 				if($location == "NL"){
 					$probabilty = 11;
 				}
 				if($probabilty > $move){
 					$location_index = rand @locations;			#Roll probability dice
 					$location = $locations[$location_index];
 				}
 			}
 			
 			open(TITLES, "Titles.txt") or die("Could not open titles file.");	#Open file containing list of titles names.
 			@titles = ();
 			#print "$node\n";
			foreach $line (<TITLES>) {
				chomp $line;
				$line =~ s/\r\n?/\n/g;
				if ($line =~ /$node/){
					@title_split = ();
					@title_split = split(':',$line);
					if($title_split[0] eq $node){
						#print "$line $title_split[0] $title_split[1]\n";
						@titles = split(',',$title_split[1]);
						break;
					}
				}	
			}
			close (TITLES);
 			
 			$job_index = $jobs_held-2;
 			$size = scalar @titles;
 			
 			if(@titles){
 				if ($size < $job_index+1){
 					#print "Size: $size\n";
 					$title = $titles[$size-1];
 					$node = $title;	
 				}else{	
 					#print "Job Index: $job_index\n";
 					$title = $titles[$job_index];
 					$node = $title
 				}
 			
 				#Print out job instance
 				#job, id, title, company, start_date, end_date, location 
 				print USERS "job,$i,$title,$company,$start_job,$end_job, $location \n";
 			}else{
 				$break = 1;
 			}
 		}
 	} 	
 }
 print "Script complete."; 