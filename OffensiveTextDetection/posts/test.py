
import fileinput
import re
import string

for line in fileinput.input():
	if(not line):
		continue;

	re.UNICODE
	line = re.sub(r'([\w+])([,.!?()])', r'\1 \2', line, flags=re.UNICODE)
	line = re.sub( r'([,.!?()])([\w+])', r'\1 \2', line, flags=re.UNICODE)
	#line = re.sub(r'[.,?!]', ' ', line)
	print line