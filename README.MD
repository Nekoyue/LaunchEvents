# Launch This Bot

The telegram bot [@LaunchThisBot](https://t.me/LaunchThisBot) is used by channel [@LaunchEvents](https://t.me/LaunchEvents). You can use it to check for upcoming rocket launches!

This bot uses information from [Launch Library 2](https://thespacedevs.com/llapi).

## Setup
1. Clone this repo.
2. Run `./gradlew shadowJar`
3. A jar file named `LaunchLib-[version]-SNAPSHOT-all.jar` should be built at `./build/libs`
4. Run `java -jar LaunchLib-[version]-SNAPSHOT-all.jar` to start the bot.
5. Edit token and ids in the `config.conf` file generated at the same directory.