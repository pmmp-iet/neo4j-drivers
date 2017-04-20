#/bin/bash
XSOCK=/tmp/.X11-unix/X0
sudo docker run -v $XSOCK:$XSOCK ubiclipse
