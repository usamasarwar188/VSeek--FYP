<?php

$file_path= 'videos\\';
$file_path=$file_path . basename($_FILES['uploaded_file']['name']);

if (move_uploaded_file($_FILES['uploaded_file']['tmp_name'],$file_path)){
    echo "Sss";

    $ip_address = $_SERVER['REMOTE_ADDR'];
    $my_command = passthru('C:\Users\hp\AppData\Local\Programs\Python\Python37\python.exe C:\Users\hp\PycharmProjects\human_activity\human_activity_reco.py -m C:\Users\hp\PycharmProjects\human_activity\resnet-34_kinetics.onnx -c C:\Users\hp\PycharmProjects\human_activity\action_recognition_kinetics.txt -i C:\xampp\htdocs\VideoUpload\videos\file2.mp4 -ip '.$ip_address);

    // echo 'User IP Address - '.$_SERVER['REMOTE_ADDR'];  

}
    else{
        echo "Failure";
        //echo 'User IP Address - '.$_SERVER['REMOTE_ADDR'];  
        // $ip_address = $_SERVER['REMOTE_ADDR'];
        //  $my_command = passthru('C:\Users\hp\AppData\Local\Programs\Python\Python37\python.exe C:\Users\hp\PycharmProjects\human_activity\human_activity_reco.py -m C:\Users\hp\PycharmProjects\human_activity\resnet-34_kinetics.onnx -c C:\Users\hp\PycharmProjects\human_activity\action_recognition_kinetics.txt -i C:\xampp\htdocs\VideoUpload\videos\file2.mp4 -ip '.$ip_address);
    
    }



?>

