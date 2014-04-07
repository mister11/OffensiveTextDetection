import fileinput
import re
import string

m1 = re.compile('[0|1|x]+')
m2 = re.compile('[#]+')

for line in fileinput.input():
	if(m1.match(line.strip()) or m2.match(line.strip())):
		continue
	print line.strip()