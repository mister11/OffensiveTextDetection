import fileinput
import re
import string

m = re.compile('[^a-zA-Z]')

for line in fileinput.input():
	if(m.match(line.strip())):
		continue
	print line.strip()