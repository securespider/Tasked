from multiprocessing.sharedctypes import Value
import socketserver
import http.server
import ssl
import requests
import protocol
import cgi
import firebase_admin

class ServerDriverHandler(http.server.BaseHTTPRequestHandler):
    def do_GET(self):
        self.send_response(404)

    def do_POST(self):
        if (self.headers['Content-Type'] != "application/json;charset=UTF-8"):
            self.send_response(404)
            return

        content_length = self.headers['Content-Length']
        post_data = self.rfile.read(int(content_length)).decode("UTF-8")
        reply = protocol.protocol_handler(post_data)
        reply_bytes = bytes(str(reply), "UTF-8")
        self.send_response(200)
        self.send_header("Content-Type", "application/json;charset=UTF-8")
        self.send_header("Content-Length", str(len(reply_bytes)))
        self.end_headers()
        self.wfile.write(reply_bytes)
        self.wfile.flush()

def main():
    httpd = socketserver.TCPServer(('0.0.0.0', 10001), ServerDriverHandler)
    httpd.serve_forever()

if __name__ == "__main__":
    main()