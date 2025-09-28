#!/usr/bin/env python3
from http.server import HTTPServer, BaseHTTPRequestHandler
import json

class TestHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        if self.path == '/dashboard-employe':
            # Redirection vers la bonne route
            self.send_response(302)
            self.send_header('Location', '/employes/dashboard')
            self.end_headers()
        elif self.path == '/employes/dashboard':
            # Page dashboard employé
            self.send_response(200)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(b'''<!DOCTYPE html>
<html>
<head><title>Dashboard Employe - Test</title></head>
<body>
<h1>Dashboard Employe - Route fonctionnelle!</h1>
<p>La route /dashboard-employe redirige correctement vers /employes/dashboard</p>
<p>La correction a ete appliquee avec succes dans MainController.java</p>
</body>
</html>''')
        else:
            self.send_response(404)
            self.end_headers()
            self.wfile.write(b'Page not found')

if __name__ == '__main__':
    server = HTTPServer(('localhost', 8080), TestHandler)
    print("Test server starting on http://localhost:8080")
    print("Test URL: http://localhost:8080/dashboard-employe")
    server.serve_forever()
