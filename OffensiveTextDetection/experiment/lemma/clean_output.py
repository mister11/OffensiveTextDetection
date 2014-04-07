import fileinput
import re
import string

m = re.compile('[a-zA-Z0-9]', flags=re.UNICODE)

for line in fileinput.input():
	if(not m.match(line.strip())):
		continue
	print line.strip()