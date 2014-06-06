$users = 10;
@first_names = ();
@last_names = ();
@months = ("1", "2", "3","4","5","6","7","8","9","10","11","12");
@days_10s = ("0","1","2");  #ignoring 30th and 31st
@days_1s = ("1","2","3","4","5","6","7","8","9");  #ignoring 10th and 20th
@mail = ('@gmail.com', '@hotmail.com', '@yahoo.com');
@gender = ("M","F");


open(FIRST_NAMES, "First_Names.csv") or die("Could not open first names file.");
foreach $line (<FIRST_NAMES>) {
 	chomp;
 	$line =~ s/^\s+|\s+$//g;
 	push(@first_names, $line); 
 }
 close (FIRST_NAMES);
 

 
open(LAST_NAMES, "Last_Names.csv") or die("Could not open first names file.");
foreach $line (<LAST_NAMES>) {
 	chomp;
 	$line =~ s/^\s+|\s+$//g;
 	push(@last_names, $line); 
 }
 close (LAST_NAMES);
 
open(YEARS, "Years.csv") or die("Could not open first names file.");
foreach $line (<YEARS>) {
 	chomp;
 	$line =~ s/^\s+|\s+$//g;
 	push(@years, $line); 
 }
 close (LAST_NAMES);

 for($i=0;$i<$users;$i++){
 	$index   = rand @first_names;
 	$first_name = $first_names[$index];
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
 	for($n=0;$n<10;$n++){
 		$index_num = rand @days_1s;
 		$num = "$num$days_1s[$index_num]";
 		if($n == 2 or $n == 5){
 			$num = "$num-";
 		}
 	}
 	#profile, id, first name, last name, sex, birthday, e-mail,  phone number
 	print "profile,$i,$first_name,$last_name,$sex,$month/$day/$year,$email,$num \n";
 	
 	
 	@age_exit_hs = (16,17,17,18,18,18,18,18,19,19);
 	@years_undergrad = (3,4,4,4,4,4,5,5,5,6);
 	@years_masters = (1,2);
 	@years_phd = (1,2,3,3,3);
 	@years_job = (1,2,2,3,3,3,4,4,4,5,5,5,5,6,6,6,7,7,7,8,8,9,9,10,15,20);
 	@prob = (1,2,3,4,5,6,7,8,9,10);
 	
 	$Current_Year = "2014";
 	$age = $Current_Year - $year;
 	$time_index = rand @age_exit_hs;
 	$hs = $age_exit_hs[$time_index];
 	$time = $age - $hs;
 	$jobs_held = 0;
 	$UG = 0;
 	$MS = 0;
 	$PHD = 0;
 	 	
 	$start = $year + $hs;
 	while($time > 0){
 		
 		$prob_index = rand @prob;
 		$probability = $prob[$prob_index];
 		$chance = $jobs_held + 1;
 		if ($UG < 1 && $probability > $chance ){
 			$UG = 1;
 			$start_ug = $start;
 			$end_ug_index = rand @years_undergrad;
 			$end_ug_year = $years_undergrad[$end_ug_index];
 			$end_ug = $start_ug + $end_ug_year;
 			$start = $end_ug;
 			if($end_ug >= $Current_Year){
 				$time = 0;
 				$end_ug = "Current";
 			}else{
 				$time = $time - $end_ug_year;
 			}
 			#education, id, degree, specialization, school, start_date, end_date
 			print "education,$i,Bacholers,specialization,school,$start_ug,$end_ug \n";
 		}
 	
 		$prob_index = rand @prob;
 		$probability = $prob[$prob_index];
 		$chance = $jobs_held + 7;
 		if($MS < 1 && $UG == 1 && $probability > $chance){
 			$MS = 1;
 			$start_ms = $start;
 			$end_ms_index = rand @years_masters;
 			$end_ms_year = $years_masters[$end_ms_index];
 			$end_ms = $start_ms + $end_ms_year;
 			$start = $end_ms;
 			if($end_ms >= $Current_Year){
 				$time = 0;
 				$end_ms = "Current";
 			}else{
 				$time = $time - $end_ms_year;
 			}
 			#education, id, degree, specialization, school, start_date, end_date
 			print "education,$i,Masters,specialization,school,$start_ms,$end_ms \n";
 		}
 	
 		$prob_index = rand @prob;
 		$probability = $prob[$prob_index];
 		$chance = $jobs_held + 7;
 		if($PHD < 1 && $MS == 1 && $probability > $chance){
 			$PHD = 1;
 			$start_phd = $start;
 			$end_phd_index = rand @years_phd;
 			$end_phd_year = $years_phd[$end_phd_index];
 			$end_phd = $start_phd + $end_phd_year;
 			$start = $end_phd;
 			if($end_phd >= $Current_Year){
 				$time = 0;
 				$end_ms = "Current";
 			}else{
 				$time = $time - $end_phd_year;
 			}
 			#education, id, degree, specialization, school, start_date, end_date
 			print "education,$i,PHD,specialization,school,$start_phd,$end_phd \n";
 		}
 		
 		$prob_index = rand @prob;
 		$probability = $prob[$prob_index];
 		$chance = $jobs_held + 4;
 		if($probability <= $chance){
 			$start_job = $start;
 			$end_job_index = rand @years_job;
 			$end_job_year = $years_job[$end_job_index];
 			$end_job = $start_job + $end_job_year;
 			$start = $end_job;
 			if($end_job >= $Current_Year){
 				$time = 0;
 				$end_job = "Current";
 			}else{
 				$time = $time - $end_job_year;
 			}
 			$jobs_held++;
 			#job, id, title, company, location, start_date, end_date
 			print "job,$i,title,company,location,$start_job,$end_job \n";
 		}
 	}  	
 }