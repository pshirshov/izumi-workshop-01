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

A snippet we are gonna reuse: [Bifunctor IO wrapper](lib/common/src/main/scala/com/github/pshirshov/izumi/workshop/w01/BifunctorIO.scala)


Wanna more?
-----------

1. Relevant slides: https://github.com/7mind/slides/blob/master/02-roles/target/roles.pdf
2. The Principle behind: https://github.com/7mind/slides/blob/master/03-pper-basics/target/pper-base.pdf
3. Toolkit: https://github.com/pshirshov/izumi-r2
4. Docs: https://izumi.7mind.io/
5. Company: https://7mind.io



