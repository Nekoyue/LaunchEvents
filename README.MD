# Launch This Bot

Telegram bot [@LaunchThisBot](https://t.me/LaunchThisBot) for channel [@LaunchEvents](https://t.me/LaunchEvents). You can use it to check for upcoming rocket launches.

This bot uses [Launch Library 2](https://thespacedevs.com/llapi) API.

## Setup
1. Clone this repo to a local machine.
2. Execute `./gradlew shadowJar` in CLI to start building.
3. Wait for completion. A file names `LaunchLib-[version]-SNAPSHOT-all.jar` should be generated under `./build/libs`.
4. Change directory and execute `java -jar LaunchLib-[version]-SNAPSHOT-all.jar` to start the bot.
5. Edit Telegram token and ids in `config.conf`, then restart the bot.

Note: Require Java 11+ to compile and run.