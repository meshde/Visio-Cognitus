#!/bin/python

import socket as s

server = s.socket(s.AF_INET,s.SOCK_STREAM)
server.bind(("192.168.100.5",8080))
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
while size < int(length[2:]):
 rec = client.recv(39295)
 print size
 size += len(rec)
 data += rec
print len(data)
#print "Recieved message: "+ str(data)
with open('file2.jpg','wb') as f:
 f.write(data)


