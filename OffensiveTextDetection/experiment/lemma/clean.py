#makes spaces between punctuations and words

import fileinput
import re
import string

for line in fileinput.input():
	if(not line):
		continue;

	#line = re.sub(r'(\w+)([,.!?()]\"\')', r'\1 \2', line, flags=re.UNICODE)
	line = re.sub(r'([a-zA-Z0-9]+)([,.!?()\"\'%-])', r'\1 \2', line, flags=re.UNICODE)
	line = re.sub( r'([,.!?()\"\%-])([a-zA-Z0-9]+)', r'\1 \2', line, flags=re.UNICODE)
	#line = re.sub(r'[.,?!]', ' ', line)
	print line