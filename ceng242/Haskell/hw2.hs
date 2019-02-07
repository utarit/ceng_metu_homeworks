module Hw2 where

import Data.List -- YOU MAY USE THIS MODULE FOR SORTING THE AGENTS

data Level = Newbie | Intermediate | Expert deriving (Enum, Eq, Ord, Show, Read)
data Hunter = Hunter {hID::Int, hlevel::Level, hEnergy::Int, hNumberOfCatches::Int, hActions::[Direction]} deriving (Eq, Show, Read)
data Prey = Prey {pID::Int, pEnergy::Int, pActions::[Direction]} deriving (Eq, Show, Read)
data Cell = O | X | H Hunter | P Prey | T deriving (Eq, Show, Read)
data Direction = N | S | E | W deriving (Eq, Show, Read)
type Coordinate = (Int, Int)
-- DO NOT CHANGE THE DEFINITIONS ABOVE. --


-- INSTANCES OF Ord FOR SORTING, UNCOMMENT AND COMPLETE THE IMPLEMENTATIONS --
instance Ord Hunter where
   compare (Hunter a b c d _) (Hunter x y z w _)
        | b /= y = compare b y
        | c /= z = compare c z
        | d /= w = compare d w
        | a < x = GT
        | a > x = LT

instance Ord Prey where
  compare (Prey x a _ ) (Prey y b _)
        | a /= b = compare a b
        | x < y = GT
        | x > y = LT


-- WRITE THE REST OF YOUR CODE HERE --


    



