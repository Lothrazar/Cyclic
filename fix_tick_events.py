import os, glob

for f in glob.glob('src/main/java/com/lothrazar/cyclic/potion/**/*.java', recursive=True):
    with open(f, 'r') as file:
        content = file.read()
    
    modified = False
    
    if 'LivingTickEvent' in content:
        content = content.replace('import net.neoforged.neoforge.event.entity.living.LivingEvent.LivingTickEvent;', 'import net.neoforged.neoforge.event.tick.EntityTickEvent;')
        content = content.replace('LivingTickEvent event', 'EntityTickEvent.Pre event')
        content = content.replace('event.getEntity()', 'event.getEntity()') # No change here, but verify
        # Need to cast if they use event.getEntity() as LivingEntity directly, but getEntity() usually returns Entity
        # so if there is event.getEntity() we should ensure it's casted or checked.
        # Most scripts use event.getEntity() and already assign it to a LivingEntity variable or check.
        modified = True

    if modified:
        with open(f, 'w') as file:
            file.write(content)
