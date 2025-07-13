#!/bin/bash

echo "===== Cashi Payment API Performance Test ====="
echo "This test simulates 5 concurrent users making payments"
echo "Each user will make 10 payment requests"
echo ""

# Check if JMeter is installed
if ! command -v jmeter &> /dev/null; then
    echo "JMeter is not installed. Please install JMeter first."
    echo "You can download it from: https://jmeter.apache.org/download_jmeter.cgi"
    exit 1
fi

# Check if backend is running
if ! curl -s http://localhost:8080/health > /dev/null; then
    echo "Backend server is not running!"
    echo "Please start the backend with: ./gradlew :backend:run"
    exit 1
fi

echo "Starting performance test..."
echo ""

# Run JMeter test
jmeter -n -t payment-api-test.jmx -l results.jtl -e -o report/

echo ""
echo "Test completed!"
echo "Results saved to: results.jtl"
echo "HTML report generated in: report/index.html"
echo ""
echo "Summary:"
tail -n 1 results.jtl