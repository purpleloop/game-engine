# GameEngine - Making a Swing action game

When we want to create an action game with the game engine, the first thing to do is to create a new Java project.

Let name it _Sample_ for instance here.

# Maven dependencies

We will use Apache Maven as dependency management tool.

It is necessary to add the dependency on _game-engine-action-swing_ :

        <dependency>
            <groupId>io.github.purpleloop.gameengine</groupId>
            <artifactId>game-engine-action-swing</artifactId>
            <version>xxx</version>
        </dependency>

# Main class

We need a bootstrap class _SampleMainGame_ that will create the GameMainFrame.

        public final class SampleGameMain {
            public static void main(String[] args) {
                GameMainFrame.main(args);
            }
        }


# Configuration files

Create a configuration file according to the _sample-config.xml_ Schema _game-config.xsd_ .

    
    <?xml version="1.0" encoding="UTF-8"?>
    <config
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="game-config.xsd">
        
        <properties>
            <property name="levelSetFileName" value="data/SampleLevelSet.xml"/>
        </properties>
        
        <class role="environment" classname="io.github.purpleloop.game.sample.model.SampleEnvironment"/>
        <class role="level" classname="io.github.purpleloop.game.sample.model.SampleGameLevel"/>
        <class role="view" classname="io.github.purpleloop.game.sample.gui.SampleView"/>
        <class role="session" classname="io.github.purpleloop.game.sample.model.SampleSession"/>
        <class role="level_provider" classname="io.github.purpleloop.gameengine.action.model.level.PredefinedLevelSet"/>
        <class role="controller" classname="io.github.purpleloop.gameengine.action.gui.keyboard.KeyboardController"/>
        
        <keymap key="Space" action="mainAction"/>    
        <keymap key="Up" action="up"/>
        <keymap key="Down" action="down"/>
        <keymap key="Left" action="left"/>
        <keymap key="Right" action="right"/>
        
        <image name="sprites" location="sprites.gif"/>
        <sound name="sound1" location="sound1.wav"/>
    </config>

# Required classes

We need several more classes to implement the game.

Design tip : It is strongly advised to separate the model classes from the UI.

## Level

The level class ( _SampleGameLevel.java_ ) class will manage specificities of the game levels of our game.
It can implement ILevel but as it is advised to use the _PredefinedLevelSet_ class as level manager that manage XML parsing, we must implement XMLGameLevel. 
   
    public class SampleGameLevel implements XMLGameLevel {
    
        @Override
        public void loadFromXML(Element levelElement) throws Exception {
            ... here we have to build the level from XML elements
        }
        
    }

## Environment

We need a class to implement the environment ( _SampleEnvironment.java_ ).
It must at least implement the interface ISessionEnvironment.

* CommonAbstractEnvironment adds session support and controllable
* AbstractEnvironment is an extension based on a 2D Matrix
* AbstractObjectEnvironment is an alternative that adds the management of environment objects

_As the game engine results of an abstraction of several game implementations, this complexity is present for legacy reasons and this may evolve in the future._

    public class SampleEnvironment extends AbstractObjectEnvironment {
    
        public SampleEnvironment(ISession session, IGameLevel level) {
            super(session);
            ... use the level to as a static basis to create the environment
        }
    
        @Override
        public void dumpEnvironmentObjects() {        
        }
    
        @Override
        protected IAgent spawnAgentRandomly(String name) {
            // FIXME : This mandatory override should be removed (to be studied) ... 
            return null;
        }
        
    }


## Session

We need a class to implement the session ( _SampleSession.java_ ), that is the game occurrence in itself.
It must implements _ISession_ , but _BaseAbstractSession_ manages all the engine, player and other stuff...
We will place here the game logics.

    public class SampleSession extends BaseAbstractSession {
    
        public SampleSession(IGameEngine gameEngine) throws EngineException {
            super(gameEngine);
        }
    
        @Override
        public boolean isEnded() {
            return ...;
        }
    
        @Override
        public void environmentChanged(IGameEvent event) {      
        }
            
    }


## View 

The view class ( _SampleView_ ) will contain the graphical rendering of the game.
It must at least implement _IGameView_ but it is advised to extend the _BaseGameView_ abstract class.
This one provides support for game sessions, sprites, debug info ...

    public class SampleView extends BaseGameView {
    
        public SampleView(GameConfig conf, IDataFileProvider dataFileProvider, GamePanel owner) {
            super(owner, conf);
            ...
        }
    
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 500);
        }
    
        @Override
        protected void paintView(Graphics g) {
              ... paint here according to the current session
        }
    
    }

## Java modules

The java module lists things that must be imported and exported.

It is important to expose the classes defined in the configuration to the game engine package since they are instantiated in a reflexive manner.

    /** Sample module. */
    module sample {
        exports sample.gui to io.github.purpleloop.commons;
        exports sample.model to io.github.purpleloop.commons, game.engine.action;
        
        requires commons.logging;
        requires org.apache.logging.log4j;
        requires org.apache.logging.log4j.core;
        requires org.apache.logging.log4j.jcl;
        requires io.github.purpleloop.gameengine.core;
        requires io.github.purpleloop.commons.swing;
        requires game.engine.action;
        requires game.engine.action.swing;
        requires java.xml;
    }


# Sprites

We have to create a sprite set for our game.

# Sounds

We have to build a sound library with a sound sample for each event we want to emphasis.

Example of such events : 
- Open a door
- Explosion
- Spawn a character
...

# Launching the game

For launching the game, we need to execute the JVM specifying the bootstrap class and the configuration file.

    java <maven class path> SampleMainGame -csample-config.xml


# Current problems
 
## Actions
 
* Meta-actions on the game engine
* Globals, parametric ?
* event-driven

# Improvements

_in the future the game workshop may be used to ease the spriteset creation._
