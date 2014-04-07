import fileinput
import re
import string

for line in fileinput.input():
	if(not line.strip()):
		continue;
	print line.strip()