#TodoList

##Enlaces de interés

  - ###Repositorio de código (Github)
  
    * Enlace: https://github.com/manugm1/mads-todolist
    
  - ###Gestión de tickets (Trello)
  
    * Enlace: https://trello.com/b/hzG2RBc0/todolist-tickets-manuel-garcia

##Características implementadas (tickets) y capturas

  - ###Panel administrador

    * Creación usuario 
        * (TIC-2 https://github.com/manugm1/mads-todolist/commit/1aa3c243ed6cc1a8b8ef27c992fe88823fae97b9)
        * Captura crear usuario
        ![GitHub Logo](/docs/Crear usuario.png)
    * Listar usuarios 
        * (TIC-4 https://github.com/manugm1/mads-todolist/commit/9e656942380ae02318f1ecd9daca10be24878128)
        * Captura listado de usuarios
        ![GitHub Logo](/docs/Listado usuarios.png)
    * Ver detalles usuario 
        * (TIC-5 https://github.com/manugm1/mads-todolist/commit/157a41c37b7a929e9305631118fa362e389e0ce2)
        * Captura detalles de un usuario
        ![GitHub Logo](/docs/Detalle usuario.png)
    * Modificar usuario 
        * (TIC-6 https://github.com/manugm1/mads-todolist/commit/e6b2237964c461ecc9e96405966983be448f68c6)
        * Captura modificar usuario
        ![GitHub Logo](/docs/Modificar usuario.png)
        * Captura modificar correctamente usuario
        ![GitHub Logo](/docs/Modificar bien usuario.png)
    * Borrar usuario 
        * (TIC-7 https://github.com/manugm1/mads-todolist/commit/0fecccfdcc5a04ac1591b3328a4fb1322bd88c39)
        * Captura confirmar borrado usuario
        ![GitHub Logo](/docs/Confirm borrar usuario.png)
    * Plantilla boostrap 
        * (TIC-3 https://github.com/manugm1/mads-todolist/commit/2ced6cffc61a5604ba5a1555643acd5fedcbc656)
        * (TIC-9 https://github.com/manugm1/mads-todolist/commit/860d268500e80de369c201708cdaee33b546f89a)
        * Enlace plantilla bootstrap SB-Admin:https://blackrockdigital.github.io/startbootstrap-sb-admin/

  - ###Panel registro
    * Plantilla registro
        * (TIC-8 https://github.com/manugm1/mads-todolist/commit/d78e57d60802b144a4eca0a2ef52c73acdc105a9)
        * Enlace plantilla bootstrap formulario registro: http://bootsnipp.com/snippets/featured/login-and-register-tabbed-form
    * Registro usuario
        * (TIC-10 https://github.com/manugm1/mads-todolist/commit/2a7c0b50e0743a8bd1c83c4137b1c3eeca342e0d)
        * (TIC-12 https://github.com/manugm1/mads-todolist/commit/d72cd9f965c1c9c1a5bd666a50b2006c751a16ad) -> Nuevo formulario
        * Captura formulario registro
        ![GitHub Logo](/docs/Registro.png)
        * Captura errores registro (no hemos introducido bien el campo login)
        ![GitHub Logo](/docs/Errores registro.png)
        * Captura registro usuario ya existe
        ![GitHub Logo](/docs/Registro usuario existente.png)
        * Captura passwords no coinciden
        ![GitHub Logo](/docs/Registro password repetidas.png)
        * Detalles implementación: Si el usuario que intenta registrarse introduce un login que ya está registrado el sistema dará error. Si el usuario inserta un login válido pero las contraseñas no coinciden, dará error. Si el usuario que intenta registrarse introduce un login válido y las contraseñas coinciden, se registrará y se redireccionará a la página de bienvenida.
    * Login usuario
        * (TIC-11 https://github.com/manugm1/mads-todolist/commit/37b6c48cc0a53e9524bbcd981a2285ba156ea52f)
        * Captura formulario login
        ![GitHub Logo](/docs/Login.png)
        * Captura errores login (no hemos introducido bien algún campo)
        ![GitHub Logo](/docs/Errores login.png)
        * Captura login no existe (usuario que intentamos introducir no existe)
        ![GitHub Logo](/docs/Login usuario ya existe.png)
        * Captura login incorrecto (password incorrecto) 
        ![GitHub Logo](/docs/Login incorrecto.png)
        * Captura login con usuario sin contraseña
        ![GitHub Logo](/docs/Login con usuario sin contraseña.png)
        * Detalles implementación:
        Si el usuario que intenta acceder ya está registrado, se comprobará su login y contraseña. Si su contraseña en el   sistema es vacía (lo dio de alta el administrador) la misma página le indicará de registrarse al usuario con una contraseña. Ver captura login usuario sin contraseña.
        Si el usuario que se introduce no existe, devuelve un error indicándolo. Ver captura login no existe.
        El formulario también indica si los datos del login son incorrectos (en concreto, si el password es incorrecta).
        
  - ###Panel usuario
    * Bienvenida
        * (TIC-10 https://github.com/manugm1/mads-todolist/commit/2a7c0b50e0743a8bd1c83c4137b1c3eeca342e0d)
        * Captura portada (logueado correctamente el usuario)
        ![GitHub Logo](/docs/Usuario logueado correctamente.png)
        
##Detalles técnicos del login y registro

  Para las vistas se ha utilizado un plugin bootstrap para darle formato. En una misma tendríamos los dos formularios, tanto el de login como el de registro.
        
  Se ha creado otro método en UsuarioDAO para acceder con el UsuarioService. Éste es el de "findByUsuarios". Recibe dos parámetros, una clave y un valor. Supuestamente este método serviría para corregir algo que yo al menos no vi en la documentación de Play y no es más que un método que busque por un parámetro en concreto. Es decir, si pasamos como clave "fechaNacimiento" y como valor "1993-01-12", debería devolver una lista con todos los usuarios que tengan esa fecha. Este método es mejorable, ya que todos los campos no son del mismo tipo (int, varchar, text, date, time, etc.), por lo que para hacerlo bien, habría que comprobar en el método de qué tipo es la clave que se le pasa, y así poder hacer una consulta correcta, ya sea utilizando "=", "like"... También, para este caso como es comprobar si existe un usuario, el campo "login" debería ser único y devolver tan solo un objeto Usuario. Utilizando este método, devolvemos una lista y siempre habría que capturar la primera posición, tal como "usuarios[0]". Quitando estos dos puntos, la solución del método creado es válida.
        
  Por último, toda la lógica que se ha realizado tanto del login como del registro se ha realizado en el propio controlador creado para eso "LoginController". Puede ser algo mejorable ya que el método login y el método registro comparten mucho código y se tiende a copiar y pegar. Tal vez, realizar una solución más óptima en este punto, separando todavía más la lógica de la aplicación y no ponerla toda en el controlador.
        
        
##Posibles mejoras (sin implementar)

  - ###Validación de datos en formularios (todos)
  Se ha dejado pendiente la inclusión de filtros de validación como por ejemplo: correo electrónico correcto, password requerida desde registro, campos no nulos, etc. 
  
  - ###Mejorar diseño panel usuario
  El panel de usuario (actualmente solo la vista de bienvenida) es muy simple. Se ha dejado pendiente mejorarla con la inclusión de alguna plantilla html y css o directamente haciendo uso de una plantilla bootstrap.
