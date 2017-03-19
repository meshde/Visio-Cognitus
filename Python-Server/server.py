#!/bin/python

import socket as s

server = s.socket(s.AF_INET,s.SOCK_STREAM)
server.bind(("192.168.100.5",8080))
server.listen(10)
client,addr = server.accept()
print "Connected to "+ str(addr)
data = client.recv(1024)
print "Recieved message: "+ str(data)


