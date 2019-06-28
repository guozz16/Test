import socket
HOST = '192.168.112.1'
PORT = 4700

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST, PORT))
sock.send("hello".encode())
while True:
    szBuf = sock.recv(1024)
    szBuf = szBuf.decode('utf-8')
    if len(szBuf)>0 :
    	print(1)
    	sock.send('hi'.encode())

sock.close()