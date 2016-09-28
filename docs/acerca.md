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
        * Captura formulario registro
        ![GitHub Logo](/docs/Registro.png)
        * Captura errores registro (no hemos introducido bien algún campo)
        ![GitHub Logo](/docs/Errores registro.png)
        * Captura registro usuario ya existe
        ![GitHub Logo](/docs/Registro usuario existente.png)
        * Detalles implementación: Si el usuario que intenta registrarse introduce un login que ya está registrado se comprobará si está con contraseña o no. Si no tiene contraseña, se da por bueno los datos del formulario de registro y procede a tomar como válidos todos, sustituyendo todos aquellos diferentes. Si sí que tiene contraseña, mostrará error de que el usuario ya existe.
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
        * Captura portada viniendo de registro (usuario se introdució mediante el administrador)
        ![GitHub Logo](/docs/Usuario registrado correctamente, ya existia.png)

##Posibles mejoras (sin implementar)

  - ###Validación de datos en formularios (todos)
  Se ha dejado pendiente la inclusión de filtros de validación como por ejemplo: correo electrónico correcto, password requerida desde registro, campos no nulos, etc. 
  
  - ###Mejorar diseño panel usuario
  El panel de usuario (actualmente solo la vista de bienvenida) es muy simple. Se ha dejado pendiente mejorarla con la inclusión de alguna plantilla html y css o directamente haciendo uso de una plantilla bootstrap.
  
  - ###Contraseña aleatoria registro
  Cuando el usuario se registra con usuario ya existente que no tenía contraseña (introducido originalmente por el administrador), hubiera sido interesante introducir que si no se pone ninguna contraseña en el registro, se añada una aleatoria y mostrar, mediante el mensaje "flash" que le aparece al usuario en la pantalla de bienvenida, un mensaje avisándole de que revise su contraseña ya que se le ha establecido una aleatoria.
  
