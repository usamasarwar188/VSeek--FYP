import numpy as np
import argparse
import imutils
import sys
import cv2
import json
import socket



def sendLabelsToClient(jsonString,ipaddr):

	s = socket.socket()

	port = 7766

	s.connect((ipaddr, port))

	stri = "Python send this"

	s.send(jsonString.encode())

	s.close()



ap = argparse.ArgumentParser()
ap.add_argument("-m", "--model", required=True,
	help="path to trained human activity recognition model")
ap.add_argument("-c", "--classes", required=True,
	help="path to class labels file")
ap.add_argument("-i", "--input", type=str, default="",
	help="optional path to video file")
ap.add_argument("-ip", "--ipaddress", required=True,
	help="ip address")
args = vars(ap.parse_args())



print(args["ipaddress"])

CLASSES = open(args["classes"]).read().strip().split("\n")
SAMPLE_DURATION = 16
SAMPLE_SIZE = 112

# load the human activity recognition model
net = cv2.dnn.readNet(args["model"])

# grab a pointer to the input video stream
vs = cv2.VideoCapture(args["input"] if args["input"] else 0)


labels = []
breaking=False

while True:

	frames = []

	# loop over the number of required sample frames
	for i in range(0, SAMPLE_DURATION):
		# read a frame from the video stream
		(grabbed, frame) = vs.read()

		# if the frame was not grabbed
		if not grabbed:
			print("no frame read - exiting")
			breaking=True
			break
			#sys.exit(0)

		frame = imutils.resize(frame, width=400)
		frames.append(frame)

	if breaking:
		break

	blob = cv2.dnn.blobFromImages(frames, 1.0,
		(SAMPLE_SIZE, SAMPLE_SIZE), (114.7748, 107.7354, 99.4750),
		swapRB=True, crop=True)
	blob = np.transpose(blob, (1, 0, 2, 3))
	blob = np.expand_dims(blob, axis=0)

	# passing frames to the network
	net.setInput(blob)
	outputs = net.forward()

	label = CLASSES[np.argmax(outputs)]
	labels.append(label)
	# loop over our frames               uncomment it to check
	# for frame in frames:
	# 	cv2.rectangle(frame, (0, 0), (300, 40), (0, 0, 0), -1)
	# 	cv2.putText(frame, label, (10, 25), cv2.FONT_HERSHEY_SIMPLEX,
	# 		0.8, (255, 255, 255), 2)
	#
	# 	cv2.imshow("Activity Recognition", frame)
	# 	key = cv2.waitKey(1) & 0xFF
	#
	# 	if key == ord("q"):
	# 		break

jStr = json.dumps(labels)
jsonString = "{'labels': "+jStr+"}"
sendLabelsToClient(jsonString,args["ipaddress"])

print(jsonString)
