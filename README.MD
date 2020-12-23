# Launch This Bot

The telegram bot [@LaunchThisBot](https://t.me/LaunchThisBot) is used by channel [@LaunchEvents](https://t.me/LaunchEvents). You can use it to check for upcoming rocket launches!

This bot uses information from [Launch Library 2](https://thespacedevs.com/llapi) API.

## Setup
1. Clone repo to local machine.
2. Execute `./gradlew shadowJar` in CLI to start building.
3. Wait and a file names `LaunchLib-[version]-SNAPSHOT-all.jar` should be generated under `./build/libs`.
4. Change directory and execute `java -jar LaunchLib-[version]-SNAPSHOT-all.jar` to start the bot.
5. Remember to edit Telegram token and ids in `config.conf`.

Note: Require Java 11+ to compile and run.