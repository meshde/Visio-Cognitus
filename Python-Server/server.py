#!/bin/python

import socket as s
from helper import manipulate
import random
import sys

ip = sys.argv[1]
server = s.socket(s.AF_INET,s.SOCK_STREAM)
server.bind((ip,8080))
server.listen(10)
client,addr = server.accept()
print "Connected to "+ str(addr)
length = client.recv(10)
print length
#for i,a in enumerate(length[2:]):
# print str(i)+" "+a
#print type(int(length.strip()))
#print type(length.decode('utf-8'))
data = ''
size = 0
r = random.randint(1,10000)
while size < int(length[2:]):
 rec = client.recv(39295)
 print size
 size += len(rec)
 data += rec
print len(data)
#print "Recieved message: "+ str(data)
with open('file{}.png'.format(r),'wb') as f:
 f.write(data)

manipulate(r)

with open('result{}.png'.format(r),'r') as f:
	data = f.read()

client.send(data)

