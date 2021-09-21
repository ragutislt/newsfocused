# How to build

1. Build the image:
`docker build --rm -t news_focused/mailcatcher ./`
2. Run the container:
`docker run --rm --name='news_focused-mailcatcher' -d --publish=1080:1080 --publish=10025:10025 news_focused/mailcatcher`
3. Go to `http://127.0.0.1:1080/` for the GUI, send mail through smtp://127.0.0.1:10025

If it complains about previous container not removed, do:
`docker rm mailcatcher`