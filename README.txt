Prerequisites
-------------

1. JDK 8 (should work on JDK 9/10, JDK 11 isn't supported)
2. IDEA Community (or Ultimate) + Scala Plugin

How to install:

```
brew cask install caskroom/versions/java8
brew cask install java8
brew cask install intellij-idea-ce

brew install jenv
echo 'export PATH="$HOME/.jenv/bin:$PATH"' >> ~/.bash_profile
echo 'eval "$(jenv init -)"' >> ~/.bash_profile
jenv global 1.8
# restart terminal at this point
```

Wanna more?
-----------

1. Relevant slides: https://github.com/7mind/slides/blob/master/02-roles/target/roles.pdf
2. The Principle behind: https://github.com/7mind/slides/blob/master/03-pper-basics/target/pper-base.pdf
