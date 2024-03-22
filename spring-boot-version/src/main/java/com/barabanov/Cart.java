package com.barabanov;

import java.util.UUID;

public record Cart(UUID uuid, String name, Attempt attempt)
{
}
