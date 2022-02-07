# peric


What is interesting about your project? 

I learned a lot during this project. My initial idea for Project one was to animate the characters that I will use in the game. Unfortunately, I did not achieve that because I didn't really know-how. Initially, it was hard to animate the characters because the examples that I found all used sprite sheets(which I was not using) and I did not want to do that. Reading and understanding the TextureRegion, Texture, Animation libGDX documentation made it easier to figure out a way out and animate the characters. It was really exciting to see it work the way that I wanted. It felt like winning a long hard battle. Since I was not using Sprite and used Textures instead, I had to very intentionally reduce the scale of the animated characters. (basically seeing what works since Texture did not have the .scale method). I worked with getRegionX(), getRegionY(), getRegionHeight(), getRegionWidth() a lot to scale the sizes as well.
One of the things I wished I could have achieved for this project was that when I scaled the background, all the animated characters would have scaled in proportion to the new background’s size. I tried to get that perfect scale(not hard coding the values) but it was a lot and
 so I skipped that. In all, I really enjoyed having a better understanding of TextureRegion, Texture, and Animation. I think this project really taught me how to better read the documentation and instilled a better practice since I had to do it repeatedly.
 
The other thing that I had to deal with was the projectile motion. It was work that was worthwhile. I had to get reacquainted with Projectile motion and how to do it again. Before that, I had to know how the Input Processors really worked. In order to shoot a bird and let it move in a projectile motion, I needed to drag the angry bird(which really does not look angry) and then shoot that bird. There was a lot to learn in the beginning and when I finally figured it out it was a lot easier to use. I had a better understanding of the KEY. PRESSED PART of the input processor in Project One but for Project Two it was mostly working with the mouse button and its drag.

When this part was successful, I had to deal with the projectile motion. This was the most challenging part of this project. I knew how projectile motion worked before this game. By debugging at each point, I knew exactly what I needed to do to make it better but somehow I always left something out, and then I will figure it out during debugging. Overall, It was harder this semester to do a lot of Math and Physics problems because my headspace found it harder than normal to process things or do anything mental. But I was so happy to have figured it out again and also made my heart and mind happy to have been able to overcome.


In this code, I have figured out the projectile motion of the game and its wrapping. I have been able to animate the characters in the game and works pretty well.
Describe the resources you used. Did you learn a technique from a random website? Did you borrow graphics from somewhere? Did you get inspired by an existing game or a video you watched?

Sources: 

How to create a projectile motion of a cannonball in top down ...https://gamedev.stackexchange.com › questions › how-t...
Drawing a projectile trajectory like Angry Birds using LibGDXhttps://blog.gemserk.com › 2012/07/03 › drawing-a-pr...
