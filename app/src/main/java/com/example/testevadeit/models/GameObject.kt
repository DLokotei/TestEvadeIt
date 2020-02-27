package com.example.testevadeit.models

import com.example.testevadeit.traits.AsRectangle
import com.example.testevadeit.traits.Collidable
import com.example.testevadeit.traits.Movable

interface GameObject : Movable, Collidable, AsRectangle