import socketserver
import http.server
import ssl
import requests
import protocol
import cgi

class ServerDriverHandler(http.server.SimpleHTTPRequestHandler):
    def do_GET(self):
        self.send_response(404)

    def do_POST(self):
        if self.path.endswith('/new'):
            ctype, data = cgi.parse_header(self.headers.get('content-type'))
            if (ctype == 'text/plain'):
                print(data)
        response = self.respon

        self.send_response(200)
        self.send_header("Content-type", "application/json")
        self.end_headers()

def main():
    httpd = socketserver.TCPServer(('0.0.0.0', 10001), ServerDriverHandler)
    httpd.socket = ssl.wrap_socket(httpd.socket, certfile='new.pem', keyfile='private.key', server_side=True)
    httpd.serve_forever()

if __name__ == "__main__":
    main()